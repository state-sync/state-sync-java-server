package org.statesync;

import org.statesync.info.SyncSessionInfo;
import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

public class SyncSession {
	public final String sessionToken;
	public SyncService service;
	public String externalSessionId;
	public final SyncServiceUser user;

	public SyncSession(final SyncService service, final SyncServiceUser user, final String sessionToken,
			final String externalSessionId) {
		this.service = service;
		this.user = user;
		this.sessionToken = sessionToken;
		this.externalSessionId = externalSessionId;
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public boolean disconnectUser(final String userId) {
		return this.user.userId.equals(userId);
	}

	public SyncSessionInfo getInfo() {
		return new SyncSessionInfo(this.sessionToken, this.externalSessionId);
	}

	public void handle(final RequestMessage event) {
		switch (event.getType()) {
			case subscribeArea:
				subscribeArea((SubscribeAreaRequest) event);
				return;
			case unsubscribeArea:
				unsubscribeArea((UnsubscribeAreaRequest) event);
				return;
			case signal:
				signal((SignalRequest) event);
				return;
			case p:
			default:
				patchArea((PatchAreaRequest) event);
				return;
		}
	}

	public InitSessionResponse init() {
		return new InitSessionResponse(this.sessionToken, this.user.userToken);
	}

	private void patchArea(final PatchAreaRequest event) {
		this.service.findArea(event.area).patchArea(this, event);
	}

	private void signal(final SignalRequest event) {
		this.service.findArea(event.area).signal(this, event);
	}

	private void subscribeArea(final SubscribeAreaRequest event) {
		this.service.findArea(event.area).subscribeSession(this, event);
	}

	private void unsubscribeArea(final UnsubscribeAreaRequest event) {
		this.service.findArea(event.area).unsubscribeSession(this, event);
	}
}
