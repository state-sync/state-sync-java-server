package org.statesync;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

public class SyncSession {
	public final String userId;
	public final String sessionId;
	public SyncService service;

	public SyncSession(final SyncService service, final String userId, final String sessionId) {
		this.service = service;
		this.userId = userId;
		this.sessionId = sessionId;
	}

	public void handle(final RequestMessage event) {
		switch (event.getType()) {
			case subscribeArea:
				subscribeArea((SubscribeAreaRequest) event);
				return;
			case unsubscribeArea:
				unsubscribeArea((UnsubscribeAreaRequest) event);
				return;
			case patchArea:
			default:
				patchArea((PatchAreaRequest) event);
				return;
		}
	}

	public InitSessionResponse init() {
		return new InitSessionResponse(this.sessionId);
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
