package org.statesync;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map session to two kind of keys. Two keys are required to avoid possible
 * security issues with simple external session key (autoincremented id for
 * example).
 *
 * @author ify
 *
 */
public class SessionMap {
	/**
	 * Store session by token
	 */
	private final Map<String, SyncServiceSession> byToken = new ConcurrentHashMap<>();
	/**
	 * Store session by external session id
	 */
	private final Map<String, SyncServiceSession> byExternalSessionId = new ConcurrentHashMap<>();

	/**
	 * Add session and store in by two keys
	 *
	 * @param session
	 *            - session to store
	 */
	public void add(final SyncServiceSession session) {
		this.byToken.put(session.sessionToken, session);
		this.byExternalSessionId.put(session.externalSessionId, session);
	}

	/**
	 * Returns session by session token
	 *
	 * @param sessionToken
	 * @return session or null
	 */
	public SyncServiceSession getByToken(final String sessionToken) {
		return this.byToken.get(sessionToken);
	}

	/**
	 * Returns collection of all sessions
	 *
	 * @return sessions
	 */
	public Collection<SyncServiceSession> getSessions() {
		return this.byToken.values();
	}

	/**
	 * Remove session by external id
	 *
	 * @param externalSessionId
	 * @throws SyncException
	 *             if sessionId is unknown
	 * @return
	 */
	public synchronized SyncServiceSession removeByExternalSessionId(final String externalSessionId) {
		final SyncServiceSession session = this.byExternalSessionId.remove(externalSessionId);
		if (session != null) {
			this.byToken.remove(session.sessionToken);
		}
		return session;
	}

	/**
	 * Remove all user sessions
	 *
	 * @param userId
	 */
	public synchronized void removeByUserId(final String userId) {
		this.byToken.entrySet().removeIf(entry -> {
			final SyncServiceSession session = entry.getValue();
			if (session == null)
				throw new SyncException("Unknown session for user:" + userId);
			if (session.userId.equals(userId)) {
				this.byExternalSessionId.remove(session.externalSessionId);
				return true;
			} else {
				return false;
			}
		});
	}

	/**
	 * Returns count of sessions
	 *
	 * @return count of sessions
	 */
	public int size() {
		return this.byToken.size();
	}

}
