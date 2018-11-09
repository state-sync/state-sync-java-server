
package org.statesync.protocol.subscription;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SubscribeAreaResponse extends ResponseMessage
{
	public final String area;
	public final ClientAreaConfig config;
	public final ObjectNode model;

	public SubscribeAreaResponse(final int forId, final String area, final ClientAreaConfig config,
			final ObjectNode model)
	{
		super(forId, ResponseType.areaSubscription);
		this.area = area;
		this.config = config;
		this.model = model;
	}

	public String getArea()
	{
		return this.area;
	}

	public ClientAreaConfig getConfig()
	{
		return this.config;
	}

	public ObjectNode getModel()
	{
		return this.model;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SubscribeAreaResponse(area=" + this.getArea() + ", config=" + this.getConfig() + ", model="
				+ this.getModel() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SubscribeAreaResponse)) return false;
		final SubscribeAreaResponse other = (SubscribeAreaResponse) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$area = this.getArea();
		final java.lang.Object other$area = other.getArea();
		if (this$area == null ? other$area != null : !this$area.equals(other$area)) return false;
		final java.lang.Object this$config = this.getConfig();
		final java.lang.Object other$config = other.getConfig();
		if (this$config == null ? other$config != null : !this$config.equals(other$config)) return false;
		final java.lang.Object this$model = this.getModel();
		final java.lang.Object other$model = other.getModel();
		if (this$model == null ? other$model != null : !this$model.equals(other$model)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SubscribeAreaResponse;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $area = this.getArea();
		result = result * PRIME + ($area == null ? 43 : $area.hashCode());
		final java.lang.Object $config = this.getConfig();
		result = result * PRIME + ($config == null ? 43 : $config.hashCode());
		final java.lang.Object $model = this.getModel();
		result = result * PRIME + ($model == null ? 43 : $model.hashCode());
		return result;
	}
}
