package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;

import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public class SyncService {
	final Map<String, SyncArea<?>> areas = new ConcurrentHashMap<>();
	final SessionMap sessions = new SessionMap();

	SyncOutbound protocol;

	public SyncService(final @NonNull SyncOutbound protocol) {
		this.protocol = protocol;
	}

	public InitSessionResponse connect(@NonNull final String userId, final String externalSessionId,
			final String sessionToken) {

		final SyncServiceSession session = new SyncServiceSession(this, userId, sessionToken, externalSessionId);
		this.sessions.add(session);
		log.log(Level.SEVERE, "user " + userId + " connected with sessionToken=" + session.sessionToken
				+ " and external sessionId=" + session.externalSessionId);
		return session.init();
	}

	public void disconnectSession(final String externalSessionId) {
		this.sessions.removeByExternalSessionId(externalSessionId);
	}

	public void disconnectUser(final String userId) {
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

	public int getSessionsCount() {
		return this.sessions.size();
	}

	public void handle(@NonNull final String sessionToken, final RequestMessage event) {
		final SyncServiceSession session = this.sessions.getByToken(sessionToken);
		if (session == null) {
			throw new SyncException("Unknown sessionToken:" + sessionToken);
		}
		try {
			session.handle(event);
		} catch (final Exception e) {
			log.log(Level.SEVERE, "error", e);
			throw new SyncException(e);
		}
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

	public void fireLocalChanges(final Dependency dependency, final SyncAreaApi<?> user) {
		this.areas.values().forEach(area -> area.handleLocalChanges(dependency, user));
	}
}
