
package org.statesync.info;

import java.util.ArrayList;
import java.util.List;

public class StateSyncInfo
{
	public List<String> userIds;
	public final List<SyncSessionInfo> sessions = new ArrayList<>();
	public final List<SyncAreaInfo> areas = new ArrayList<>();

	public StateSyncInfo()
	{
	}

	public List<String> getUserIds()
	{
		return this.userIds;
	}

	public List<SyncSessionInfo> getSessions()
	{
		return this.sessions;
	}

	public List<SyncAreaInfo> getAreas()
	{
		return this.areas;
	}

	public void setUserIds(final List<String> userIds)
	{
		this.userIds = userIds;
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof StateSyncInfo)) return false;
		final StateSyncInfo other = (StateSyncInfo) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$userIds = this.getUserIds();
		final java.lang.Object other$userIds = other.getUserIds();
		if (this$userIds == null ? other$userIds != null : !this$userIds.equals(other$userIds)) return false;
		final java.lang.Object this$sessions = this.getSessions();
		final java.lang.Object other$sessions = other.getSessions();
		if (this$sessions == null ? other$sessions != null : !this$sessions.equals(other$sessions)) return false;
		final java.lang.Object this$areas = this.getAreas();
		final java.lang.Object other$areas = other.getAreas();
		if (this$areas == null ? other$areas != null : !this$areas.equals(other$areas)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof StateSyncInfo;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $userIds = this.getUserIds();
		result = result * PRIME + ($userIds == null ? 43 : $userIds.hashCode());
		final java.lang.Object $sessions = this.getSessions();
		result = result * PRIME + ($sessions == null ? 43 : $sessions.hashCode());
		final java.lang.Object $areas = this.getAreas();
		result = result * PRIME + ($areas == null ? 43 : $areas.hashCode());
		return result;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "StateSyncInfo(userIds=" + this.getUserIds() + ", sessions=" + this.getSessions() + ", areas="
				+ this.getAreas() + ")";
	}
}
