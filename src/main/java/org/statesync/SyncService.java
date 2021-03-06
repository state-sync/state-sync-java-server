
package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;

public class SyncService
{

	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(SyncService.class.getName());
	final Map<String, SyncArea<?>> areas = new ConcurrentHashMap<>();
	final SessionMap sessions = new SessionMap();
	SyncOutbound protocol;
	private Executor executor;

	public SyncService(final SyncOutbound protocol, final Supplier<ThreadContextInheritance> contextInheritance,
			final int threadsCount)
	{
		if (protocol == null) { throw new java.lang.NullPointerException("protocol"); }
		this.protocol = protocol;
		this.executor = new Executor(contextInheritance, threadsCount);
	}

	public InitSessionResponse connect(final String userId, final String externalSessionId, final String sessionToken)
	{
		if (userId == null) { throw new java.lang.NullPointerException("userId"); }
		final SyncServiceSession session = new SyncServiceSession(this, userId, sessionToken, externalSessionId,
				this.executor);
		this.sessions.add(session);
		log.log(Level.SEVERE, "user " + userId + " connected with sessionToken=" + session.sessionToken
				+ " and external sessionId=" + session.externalSessionId);
		return session.init();
	}

	public void disconnectSession(final String externalSessionId)
	{
		this.sessions.removeByExternalSessionId(externalSessionId);
	}

	public void disconnectUser(final String userId)
	{
		this.sessions.removeByUserId(userId);
	}

	public SyncArea<?> findArea(final String area)
	{
		final SyncArea<?> syncArea = this.areas.get(area);
		if (syncArea == null) { throw new SyncException("Unknown sync area:" + area); }
		return syncArea;
	}

	public int getAreasCount()
	{
		return this.areas.size();
	}

	public int getSessionsCount()
	{
		return this.sessions.size();
	}

	public void handle(final String sessionToken, final RequestMessage event)
	{
		if (sessionToken == null) { throw new java.lang.NullPointerException("sessionToken"); }
		final SyncServiceSession session = this.sessions.getByToken(sessionToken);
		if (session == null) { throw new SyncException("Unknown sessionToken:" + sessionToken); }
		try
		{
			session.handle(event);
		}
		catch (final Exception e)
		{
			log.log(Level.SEVERE, "error", e);
			throw new SyncException(e);
		}
	}

	//
	// protected String newSessionToken(final SyncServiceUser user) {
	// return UUID.randomUUID().toString();
	// }
	public void register(final SyncArea<?> area)
	{
		if (this.areas.containsKey(
				area.getAreaId())) { throw new SyncException("Redeclaration of area:" + area.getAreaId()); }
		this.areas.putIfAbsent(area.getAreaId(), area);
		area.onRegister(this);
	}

	public void unregister(final String areaId)
	{
		this.areas.remove(areaId).unregister();
	}

	public void fireLocalChanges(final Dependency dependency, final SyncAreaApi<?> user)
	{
		this.areas.values().forEach(area -> area.handleLocalChanges(dependency, user));
	}

	public void logout(final String userId)
	{
		disconnectUser(userId);
	}
}
