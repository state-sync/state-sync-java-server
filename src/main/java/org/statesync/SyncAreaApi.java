package org.statesync;

import java.util.HashSet;
import java.util.Set;

public class SyncAreaApi<Model>
{
	private final Set<Dependency> dependencies = new HashSet<>();

	private final SyncArea<Model> area;

	private final SyncServiceSession session;

	public SyncAreaApi(final SyncServiceSession session, final SyncArea<Model> area)
	{
		super();
		this.session = session;
		this.area = area;
	}

	public String getUserId()
	{
		return this.session.userId;
	}

	public void cleanDependencies()
	{
		this.dependencies.clear();

	}

	public void listen(final Dependency... dependency)
	{
		for (final Dependency d : dependency)
		{
			this.dependencies.add(d);
		}
	}

	public void listenForUser(final Dependency... dependency)
	{
		for (final Dependency d : dependency)
		{
			this.dependencies.add(d.child(getUserId()));
		}
	}

	public void fire(final Dependency dependency)
	{
		this.area.fireChanges(dependency, this);
	}

	public String getAccessToken()
	{
		return this.session.sessionToken;
	}

	public boolean hasDependency(final Dependency dependency)
	{
		return this.dependencies.contains(dependency);
	}

	public void fireForUser(final Dependency dependency)
	{
		fire(dependency.child(this.getUserId()));
	}

	public void logout()
	{
		this.session.logout();
	}
}
