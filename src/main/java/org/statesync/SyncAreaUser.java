package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SyncAreaUser {
	public final Map<String, SyncSession> sessions = new ConcurrentHashMap<>();

	public final String userId;

	public final String userToken;

	public SyncAreaUser(final String userId, final String userToken) {
		super();
		this.userId = userId;
		this.userToken = userToken;
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public boolean remove(final SyncSession session) {
		this.sessions.remove(session.sessionToken);
		return this.sessions.isEmpty();
	}
}
