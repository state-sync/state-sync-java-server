package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

@Data
public class SyncAreaUser {
	public final Map<String, SyncSession> sessions = new ConcurrentHashMap<>();
	public final String userId;

	public boolean remove(final SyncSession session) {
		this.sessions.remove(session.sessionId);
		return this.sessions.isEmpty();
	}
}
