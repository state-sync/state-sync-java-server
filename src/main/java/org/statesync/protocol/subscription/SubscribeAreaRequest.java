
package org.statesync.protocol.subscription;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

public class SubscribeAreaRequest extends RequestMessage
{
	public SubscribeAreaRequest(final int id, final String area)
	{
		super(id, RequestType.subscribeArea, area);
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SubscribeAreaRequest()";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SubscribeAreaRequest)) return false;
		final SubscribeAreaRequest other = (SubscribeAreaRequest) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SubscribeAreaRequest;
	}

	@java.lang.Override

	public int hashCode()
	{
		int result = super.hashCode();
		return result;
	}

	public SubscribeAreaRequest()
	{
	}
}
