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
	private final Map<String, SyncSession> byToken = new ConcurrentHashMap<>();
	/**
	 * Store session by external session id
	 */
	private final Map<String, SyncSession> byExternalSessionId = new ConcurrentHashMap<>();

	/**
	 * Accept visitor
	 *
	 * @param visitor
	 */
	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Add session and store in by two keys
	 *
	 * @param session
	 *            - session to store
	 */
	public void add(final SyncSession session) {
		this.byToken.put(session.sessionToken, session);
		this.byExternalSessionId.put(session.externalSessionId, session);
	}

	/**
	 * Returns session by session token
	 *
	 * @param sessionToken
	 * @return session or null
	 */
	public SyncSession getByToken(final String sessionToken) {
		return this.byToken.get(sessionToken);
	}

	/**
	 * Returns collection of all sessions
	 *
	 * @return sessions
	 */
	public Collection<SyncSession> getSessions() {
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
	public synchronized SyncSession removeByExternalSessionId(final String externalSessionId) {
		final SyncSession session = this.byExternalSessionId.remove(externalSessionId);
		if (session == null)
			throw new SyncException("Unknown session for external session id:" + externalSessionId);

		this.byToken.remove(session.sessionToken);
		return session;
	}

	/**
	 * Remove all user sessions
	 *
	 * @param userId
	 */
	public synchronized void removeByUserId(final String userId) {
		this.byToken.entrySet().removeIf(entry -> {
			final SyncSession session = entry.getValue();
			if (session == null)
				throw new SyncException("Unknown session for user:" + userId);
			if (session.disconnectUser(userId)) {
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
