package org.statesync;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

public class SyncSession {
	public final String userId;
	public final String sessionToken;
	public SyncService service;
	public String userToken;
	public String externalSessionId;

	public SyncSession(final SyncService service, final String userId, final String sessionToken,
			final String userToken, final String externalSessionId) {
		this.service = service;
		this.userId = userId;
		this.sessionToken = sessionToken;
		this.userToken = userToken;
		this.externalSessionId = externalSessionId;
	}

	public void handle(final RequestMessage event) {
		switch (event.getType()) {
			case subscribeArea:
				subscribeArea((SubscribeAreaRequest) event);
				return;
			case unsubscribeArea:
				unsubscribeArea((UnsubscribeAreaRequest) event);
				return;
			case p:
			default:
				patchArea((PatchAreaRequest) event);
				return;
		}
	}

	public InitSessionResponse init() {
		return new InitSessionResponse(this.sessionToken, this.userToken);
	}

	private void patchArea(final PatchAreaRequest event) {
		this.service.areas.get(event.area).patchArea(this, event);
	}

	private void subscribeArea(final SubscribeAreaRequest event) {
		this.service.areas.get(event.area).subscribeSession(this, event);
	}

	private void unsubscribeArea(final UnsubscribeAreaRequest event) {
		this.service.areas.get(event.area).unsubscribeSession(this, event);
	}
}
