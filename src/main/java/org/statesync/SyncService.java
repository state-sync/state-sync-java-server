package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.statesync.info.StateSyncInfo;
import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;

import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class SyncService {
	final Map<String, SyncArea<?>> areas = new ConcurrentHashMap<>();
	final SessionMap sessions = new SessionMap();
	final Map<String, SyncServiceUser> users = new ConcurrentHashMap<>();

	SyncOutbound protocol;

	public SyncService(final @NonNull SyncOutbound protocol) {
		this.protocol = protocol;
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public InitSessionResponse connect(@NonNull final String userId, final String externalSessionId,
			final String sessionToken) {
		final SyncServiceUser user = this.users.computeIfAbsent(userId, id -> new SyncServiceUser(userId));
		final SyncServiceSession session = newSession(user, externalSessionId, sessionToken);
		this.sessions.add(session);
		log.log(Level.SEVERE, "user " + userId + " connected with sessionToken=" + session.sessionToken
				+ " and external sessionId=" + session.externalSessionId);
		return session.init();
	}

	public void disconnectSession(final String externalSessionId) {
		final SyncServiceSession session = this.sessions.removeByExternalSessionId(externalSessionId);
		final String sessionToken = session.sessionToken;
		this.users.entrySet().removeIf(entry -> entry.getValue().removeSession(sessionToken));
		this.areas.values().forEach(area -> area.removeSession(sessionToken));
	}

	public void disconnectUser(final String userId) {
		this.users.remove(userId);
		this.sessions.removeByUserId(userId);
	}

	public SyncArea<?> findArea(final String area) {
		final SyncArea<?> syncArea = this.areas.get(area);
		if (syncArea == null) {
			throw new SyncException("Unknown sync area:" + area);
		}
		return syncArea;
	}

	public int getAreasCount() {
		return this.areas.size();
	}

	public StateSyncInfo getInfo() {
		final StateSyncInfoBuilder visitor = new StateSyncInfoBuilder();
		accept(visitor);
		return visitor.model;
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
		final SyncServiceSession session = this.sessions.getByToken(sessionToken);
		if (session == null) {
			throw new SyncException("Unknown sessionToken:" + sessionToken);
		}
		session.handle(event);
	}

	protected SyncServiceSession newSession(@NonNull final SyncServiceUser user, final String externalSessionId,
			final String sessionToken) {
		return new SyncServiceSession(this, user, sessionToken, externalSessionId);
	}
	//
	// protected String newSessionToken(final SyncServiceUser user) {
	// return UUID.randomUUID().toString();
	// }

	public void register(final SyncArea<?> area) {
		if (this.areas.containsKey(area.getAreaId())) {
			throw new SyncException("Redeclaration of area:" + area.getAreaId());
		}
		this.areas.putIfAbsent(area.getAreaId(), area);
		area.onRegister(this);
	}

	public void unregister(final String areaId) {
		this.areas.remove(areaId).unregister();
	}
}
