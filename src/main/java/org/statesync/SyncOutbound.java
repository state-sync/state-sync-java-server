package org.statesync;

import org.statesync.protocol.Message;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.patch.PatchAreaResponse;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.signal.SignalResponse;
import org.statesync.protocol.subscription.AreaSubscriptionError;
import org.statesync.protocol.subscription.SubscribeAreaError;
import org.statesync.protocol.sync.PatchAreaEvent;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface SyncOutbound {

	default void confirmPatch(final String sessionToken, final PatchAreaRequest event) {
		send(sessionToken, new PatchAreaResponse(event.id, event.area));
	}

	default void confirmSignal(final String sessionToken, final SignalRequest event) {
		send(sessionToken, new SignalResponse(event.id, event.area));
	}

	/**
	 * Send event to particular session
	 *
	 * @param sessionToken
	 *            - sessionToken
	 * @param event
	 *            - event to send
	 */
	void send(final String sessionToken, final Message event);

	default void sendSubscribeAreaFail(final String sessionToken, final int forId, final String areaId,
			final AreaSubscriptionError error) {
		send(sessionToken, new SubscribeAreaError(forId, areaId, error));
	}

	default void sendPatch(final String sessionToken, final String areaId, final ArrayNode diff) {
		final PatchAreaEvent patch = new PatchAreaEvent(areaId, diff);
		this.send(sessionToken, patch);
	}
}
