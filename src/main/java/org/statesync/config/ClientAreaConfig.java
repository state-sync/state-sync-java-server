// Generated by delombok at Tue Jul 03 21:02:45 NOVT 2018
package org.statesync.config;

/**
 * Area config on client side
 *
 * @author ify
 */
public class ClientAreaConfig {
	private String clientLocalPrefix = "$";
	private String[] clientPush = {"/"};
	private int timeout;

	@java.lang.SuppressWarnings("all")
	public ClientAreaConfig() {
	}

	@java.lang.SuppressWarnings("all")
	public String getClientLocalPrefix() {
		return this.clientLocalPrefix;
	}

	@java.lang.SuppressWarnings("all")
	public String[] getClientPush() {
		return this.clientPush;
	}

	@java.lang.SuppressWarnings("all")
	public int getTimeout() {
		return this.timeout;
	}

	@java.lang.SuppressWarnings("all")
	public void setClientLocalPrefix(final String clientLocalPrefix) {
		this.clientLocalPrefix = clientLocalPrefix;
	}

	@java.lang.SuppressWarnings("all")
	public void setClientPush(final String[] clientPush) {
		this.clientPush = clientPush;
	}

	@java.lang.SuppressWarnings("all")
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof ClientAreaConfig)) return false;
		final ClientAreaConfig other = (ClientAreaConfig) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$clientLocalPrefix = this.getClientLocalPrefix();
		final java.lang.Object other$clientLocalPrefix = other.getClientLocalPrefix();
		if (this$clientLocalPrefix == null ? other$clientLocalPrefix != null : !this$clientLocalPrefix.equals(other$clientLocalPrefix)) return false;
		if (!java.util.Arrays.deepEquals(this.getClientPush(), other.getClientPush())) return false;
		if (this.getTimeout() != other.getTimeout()) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof ClientAreaConfig;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $clientLocalPrefix = this.getClientLocalPrefix();
		result = result * PRIME + ($clientLocalPrefix == null ? 43 : $clientLocalPrefix.hashCode());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getClientPush());
		result = result * PRIME + this.getTimeout();
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ClientAreaConfig(clientLocalPrefix=" + this.getClientLocalPrefix() + ", clientPush=" + java.util.Arrays.deepToString(this.getClientPush()) + ", timeout=" + this.getTimeout() + ")";
	}
}
