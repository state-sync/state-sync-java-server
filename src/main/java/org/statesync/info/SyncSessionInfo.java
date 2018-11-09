
package org.statesync.info;

public class SyncSessionInfo
{
	String sessionToken;
	String externalSessionId;

	public String getSessionToken()
	{
		return this.sessionToken;
	}

	public String getExternalSessionId()
	{
		return this.externalSessionId;
	}

	public void setSessionToken(final String sessionToken)
	{
		this.sessionToken = sessionToken;
	}

	public void setExternalSessionId(final String externalSessionId)
	{
		this.externalSessionId = externalSessionId;
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SyncSessionInfo)) return false;
		final SyncSessionInfo other = (SyncSessionInfo) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$sessionToken = this.getSessionToken();
		final java.lang.Object other$sessionToken = other.getSessionToken();
		if (this$sessionToken == null ? other$sessionToken != null : !this$sessionToken.equals(other$sessionToken))
			return false;
		final java.lang.Object this$externalSessionId = this.getExternalSessionId();
		final java.lang.Object other$externalSessionId = other.getExternalSessionId();
		if (this$externalSessionId == null ? other$externalSessionId != null
				: !this$externalSessionId.equals(other$externalSessionId))
			return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SyncSessionInfo;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $sessionToken = this.getSessionToken();
		result = result * PRIME + ($sessionToken == null ? 43 : $sessionToken.hashCode());
		final java.lang.Object $externalSessionId = this.getExternalSessionId();
		result = result * PRIME + ($externalSessionId == null ? 43 : $externalSessionId.hashCode());
		return result;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SyncSessionInfo(sessionToken=" + this.getSessionToken() + ", externalSessionId="
				+ this.getExternalSessionId() + ")";
	}

	public SyncSessionInfo(final String sessionToken, final String externalSessionId)
	{
		this.sessionToken = sessionToken;
		this.externalSessionId = externalSessionId;
	}
}
