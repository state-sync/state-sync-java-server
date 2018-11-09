
package org.statesync.config;

public class SyncAreaConfig<Model>
{
	private String channel = "/syncArea/";
	private String clientLocalPrefix = "$";
	private String[] clientPush = { "/" };
	private int timeout = 60000;
	private String id;
	private Class<Model> model;
	private String serverLocalPrefix = "$";
	private String[] serverPush = { "/" };

	public SyncAreaConfig()
	{
	}

	public ClientAreaConfig getClientConfig()
	{
		final ClientAreaConfig cfg = new ClientAreaConfig();
		cfg.setClientLocalPrefix(this.clientLocalPrefix);
		cfg.setClientPush(this.clientPush);
		cfg.setTimeout(this.timeout);
		return cfg;
	}

	public String getChannel()
	{
		return this.channel;
	}

	public String getClientLocalPrefix()
	{
		return this.clientLocalPrefix;
	}

	public String[] getClientPush()
	{
		return this.clientPush;
	}

	public int getTimeout()
	{
		return this.timeout;
	}

	public String getId()
	{
		return this.id;
	}

	public Class<Model> getModel()
	{
		return this.model;
	}

	public String getServerLocalPrefix()
	{
		return this.serverLocalPrefix;
	}

	public String[] getServerPush()
	{
		return this.serverPush;
	}

	public void setChannel(final String channel)
	{
		this.channel = channel;
	}

	public void setClientLocalPrefix(final String clientLocalPrefix)
	{
		this.clientLocalPrefix = clientLocalPrefix;
	}

	public void setClientPush(final String[] clientPush)
	{
		this.clientPush = clientPush;
	}

	public void setTimeout(final int timeout)
	{
		this.timeout = timeout;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public void setModel(final Class<Model> model)
	{
		this.model = model;
	}

	public void setServerLocalPrefix(final String serverLocalPrefix)
	{
		this.serverLocalPrefix = serverLocalPrefix;
	}

	public void setServerPush(final String[] serverPush)
	{
		this.serverPush = serverPush;
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SyncAreaConfig)) return false;
		final SyncAreaConfig<?> other = (SyncAreaConfig<?>) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$channel = this.getChannel();
		final java.lang.Object other$channel = other.getChannel();
		if (this$channel == null ? other$channel != null : !this$channel.equals(other$channel)) return false;
		final java.lang.Object this$clientLocalPrefix = this.getClientLocalPrefix();
		final java.lang.Object other$clientLocalPrefix = other.getClientLocalPrefix();
		if (this$clientLocalPrefix == null ? other$clientLocalPrefix != null
				: !this$clientLocalPrefix.equals(other$clientLocalPrefix))
			return false;
		if (!java.util.Arrays.deepEquals(this.getClientPush(), other.getClientPush())) return false;
		if (this.getTimeout() != other.getTimeout()) return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		final java.lang.Object this$model = this.getModel();
		final java.lang.Object other$model = other.getModel();
		if (this$model == null ? other$model != null : !this$model.equals(other$model)) return false;
		final java.lang.Object this$serverLocalPrefix = this.getServerLocalPrefix();
		final java.lang.Object other$serverLocalPrefix = other.getServerLocalPrefix();
		if (this$serverLocalPrefix == null ? other$serverLocalPrefix != null
				: !this$serverLocalPrefix.equals(other$serverLocalPrefix))
			return false;
		if (!java.util.Arrays.deepEquals(this.getServerPush(), other.getServerPush())) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SyncAreaConfig;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $channel = this.getChannel();
		result = result * PRIME + ($channel == null ? 43 : $channel.hashCode());
		final java.lang.Object $clientLocalPrefix = this.getClientLocalPrefix();
		result = result * PRIME + ($clientLocalPrefix == null ? 43 : $clientLocalPrefix.hashCode());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getClientPush());
		result = result * PRIME + this.getTimeout();
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final java.lang.Object $model = this.getModel();
		result = result * PRIME + ($model == null ? 43 : $model.hashCode());
		final java.lang.Object $serverLocalPrefix = this.getServerLocalPrefix();
		result = result * PRIME + ($serverLocalPrefix == null ? 43 : $serverLocalPrefix.hashCode());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getServerPush());
		return result;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SyncAreaConfig(channel=" + this.getChannel() + ", clientLocalPrefix=" + this.getClientLocalPrefix()
				+ ", clientPush=" + java.util.Arrays.deepToString(this.getClientPush()) + ", timeout="
				+ this.getTimeout() + ", id=" + this.getId() + ", model=" + this.getModel() + ", serverLocalPrefix="
				+ this.getServerLocalPrefix() + ", serverPush=" + java.util.Arrays.deepToString(this.getServerPush())
				+ ")";
	}
}
