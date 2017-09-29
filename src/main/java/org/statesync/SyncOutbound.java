package org.statesync;

import org.statesync.protocol.EventMessage;
import org.statesync.protocol.ResponseMessage;

public interface SyncOutbound {
	/**
	 * Broadcast event to all sessions of specified user
	 *
	 * @param userId
	 *            - userId
	 * @param event
	 *            - event to send
	 */
	void broadcast(final String userId, final EventMessage event);

	/**
	 * Send event to particular session
	 *
	 * @param sessionId
	 *            - sessionId
	 * @param event
	 *            - event to send
	 */
	void send(final String sessionId, final ResponseMessage event);
}
