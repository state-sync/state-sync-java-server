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

	public InitSessionResponse connect(@NonNull final String userId, final String externalSessionId) {
		final SyncSession session = newSession(userId, externalSessionId);
		this.sessions.put(session.sessionToken, session);
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

	public void handle(@NonNull final String sessionToken, final RequestMessage event) {
		this.sessions.get(sessionToken).handle(event);
	}

	protected SyncSession newSession(@NonNull final String userId, final String externalSessionId) {
		return new SyncSession(this, userId, newSessionToken(userId), newUserToken(userId), externalSessionId);
	}

	protected String newSessionToken(final String userId) {
		return UUID.randomUUID().toString();
	}

	protected String newUserToken(final String userId) {
		return UUID.randomUUID().toString();
	}

	public void register(final SyncArea<?> area) {
		this.areas.putIfAbsent(area.getAreaId(), area);
		area.onRegister(this);
	}

	public void unregister(final String areaId) {
		this.areas.remove(areaId).unregister();
	}
}
