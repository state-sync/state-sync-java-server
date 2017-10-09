package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SyncServiceUser {
	public final String userId;
	public final String userToken;
	public final Map<String, SyncServiceSession> sessions = new ConcurrentHashMap<>();

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public void addSession(final SyncServiceSession session) {
		this.sessions.put(session.sessionToken, session);
	}

	public boolean onSessionDisconnect(final SyncServiceSession session) {
		this.sessions.remove(session.sessionToken);
		return this.sessions.isEmpty();
	}

	public void removeSession(final String sessionToken) {
		this.sessions.remove(sessionToken);
	}
}
