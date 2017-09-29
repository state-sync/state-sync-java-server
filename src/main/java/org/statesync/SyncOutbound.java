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
	void broadcast(final String userToken, final EventMessage event);

	/**
	 * Send event to particular session
	 *
	 * @param sessionToken
	 *            - sessionToken
	 * @param event
	 *            - event to send
	 */
	void send(final String sessionToken, final ResponseMessage event);
}
