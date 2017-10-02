package org.statesync;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMap {
	private final Map<String, SyncSession> byToken = new ConcurrentHashMap<>();
	private final Map<String, SyncSession> byExternalSessionId = new ConcurrentHashMap<>();

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public void add(final SyncSession session) {
		this.byToken.put(session.sessionToken, session);
		this.byExternalSessionId.put(session.externalSessionId, session);
	}

	public SyncSession getByToken(final String sessionToken) {
		return this.byToken.get(sessionToken);
	}

	public Collection<SyncSession> getSessions() {
		return this.byToken.values();
	}

	public synchronized SyncSession removeByExternalSessionId(final String sessionId) {
		final SyncSession session = this.byExternalSessionId.remove(sessionId);
		if (session == null)
			throw new SyncException("Unknown session for external session id:" + sessionId);

		this.byToken.remove(session.sessionToken);
		return session;
	}

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

	public int size() {
		return this.byToken.size();
	}

}
