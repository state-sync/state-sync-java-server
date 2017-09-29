package org.statesync;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;

import lombok.NonNull;

public class SyncService {
	final Map<String, SyncArea<?>> areas = new ConcurrentHashMap<>();
	private final Map<String, SyncSession> sessions = new ConcurrentHashMap<>();
	SyncOutbound protocol;

	public SyncService(final @NonNull SyncOutbound protocol) {
		this.protocol = protocol;
	}

	public InitSessionResponse connect(@NonNull final String userId) {
		final SyncSession session = newSession(userId);
		this.sessions.put(session.sessionId, session);
		return session.init();
	}

	public void disconnectSession(final String userId) {
		// TODO Auto-generated method stub
	}

	public void disconnectUser(final String userId) {
		// TODO Auto-generated method stub
	}

	public int getAreasCount() {
		return this.areas.size();
	}

	public int getSessionsCount() {
		return this.sessions.size();
	}

	public long getSessionSubscriptionsCount() {
		return this.areas.values().stream().map(area -> area.getSessionsCount())
				.collect(Collectors.summarizingInt(i -> i)).getSum();
	}

	public long getUserSubscriptionsCount() {
		return this.areas.values().stream().map(area -> area.getUsersCount()).collect(Collectors.summarizingInt(i -> i))
				.getSum();
	}

	public void handle(@NonNull final String sessionId, final RequestMessage event) {
		this.sessions.get(sessionId).handle(event);
	}

	protected SyncSession newSession(@NonNull final String userId) {
		return new SyncSession(this, userId, newSessionId());
	}

	protected String newSessionId() {
		return UUID.randomUUID().toString();
	}

	public void register(final SyncArea<?> area) {
		this.areas.putIfAbsent(area.getId(), area);
		area.onRegister(this);
	}

	public void unregister(final String areaId) {
		this.areas.remove(areaId).unregister();
	}
}
