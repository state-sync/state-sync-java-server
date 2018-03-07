package org.statesync;

import org.statesync.info.SyncSessionInfo;
import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

public class SyncServiceSession {
	public final String sessionToken;
	public SyncService service;
	public String externalSessionId;
	public String userId;

	public SyncServiceSession(final SyncService service, final String userId, final String sessionToken,
			final String externalSessionId) {
		this.service = service;
		this.userId = userId;
		this.sessionToken = sessionToken;
		this.externalSessionId = externalSessionId;
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
		return new InitSessionResponse(this.sessionToken);
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
