
package org.statesync.protocol.signal;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SignalRequest extends RequestMessage
{
	public String signal;
	public ObjectNode parameters;

	public SignalRequest(final int id, final String area, final String signal, final ObjectNode parameters)
	{
		super(id, RequestType.signal, area);
		this.signal = signal;
		this.parameters = parameters;
	}

	public String getSignal()
	{
		return this.signal;
	}

	public ObjectNode getParameters()
	{
		return this.parameters;
	}

	public void setSignal(final String signal)
	{
		this.signal = signal;
	}

	public void setParameters(final ObjectNode parameters)
	{
		this.parameters = parameters;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SignalRequest(signal=" + this.getSignal() + ", parameters=" + this.getParameters() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SignalRequest)) return false;
		final SignalRequest other = (SignalRequest) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$signal = this.getSignal();
		final java.lang.Object other$signal = other.getSignal();
		if (this$signal == null ? other$signal != null : !this$signal.equals(other$signal)) return false;
		final java.lang.Object this$parameters = this.getParameters();
		final java.lang.Object other$parameters = other.getParameters();
		if (this$parameters == null ? other$parameters != null : !this$parameters.equals(other$parameters))
			return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SignalRequest;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $signal = this.getSignal();
		result = result * PRIME + ($signal == null ? 43 : $signal.hashCode());
		final java.lang.Object $parameters = this.getParameters();
		result = result * PRIME + ($parameters == null ? 43 : $parameters.hashCode());
		return result;
	}

	public SignalRequest()
	{
	}
}
