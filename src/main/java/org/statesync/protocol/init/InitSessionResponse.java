
package org.statesync.protocol.init;

import org.statesync.protocol.EventMessage;
import org.statesync.protocol.EventType;

/**
 * Initialize session
 *
 * @author ify
 */
public class InitSessionResponse extends EventMessage
{
	/**
	 * Session token used by protocol, external sessionId can't be used because
	 * of security concerns
	 */
	public final String sessionToken;
	/**
	 * Protocol version
	 */
	public final String protocolVersion = "1.0";

	public InitSessionResponse(final String sessionToken)
	{
		super(EventType.init);
		this.sessionToken = sessionToken;
	}

	/**
	 * Session token used by protocol, external sessionId can't be used because
	 * of security concerns
	 */

	public String getSessionToken()
	{
		return this.sessionToken;
	}

	/**
	 * Protocol version
	 */

	public String getProtocolVersion()
	{
		return this.protocolVersion;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "InitSessionResponse(sessionToken=" + this.getSessionToken() + ", protocolVersion="
				+ this.getProtocolVersion() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof InitSessionResponse)) return false;
		final InitSessionResponse other = (InitSessionResponse) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$sessionToken = this.getSessionToken();
		final java.lang.Object other$sessionToken = other.getSessionToken();
		if (this$sessionToken == null ? other$sessionToken != null : !this$sessionToken.equals(other$sessionToken))
			return false;
		final java.lang.Object this$protocolVersion = this.getProtocolVersion();
		final java.lang.Object other$protocolVersion = other.getProtocolVersion();
		if (this$protocolVersion == null ? other$protocolVersion != null
				: !this$protocolVersion.equals(other$protocolVersion))
			return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof InitSessionResponse;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $sessionToken = this.getSessionToken();
		result = result * PRIME + ($sessionToken == null ? 43 : $sessionToken.hashCode());
		final java.lang.Object $protocolVersion = this.getProtocolVersion();
		result = result * PRIME + ($protocolVersion == null ? 43 : $protocolVersion.hashCode());
		return result;
	}
}
