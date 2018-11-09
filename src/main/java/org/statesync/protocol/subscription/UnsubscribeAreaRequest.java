
package org.statesync.protocol.subscription;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

public class UnsubscribeAreaRequest extends RequestMessage
{
	public UnsubscribeAreaRequest(final int id, final String area)
	{
		super(id, RequestType.unsubscribeArea, area);
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "UnsubscribeAreaRequest()";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof UnsubscribeAreaRequest)) return false;
		final UnsubscribeAreaRequest other = (UnsubscribeAreaRequest) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof UnsubscribeAreaRequest;
	}

	@java.lang.Override

	public int hashCode()
	{
		int result = super.hashCode();
		return result;
	}

	public UnsubscribeAreaRequest()
	{
	}
}
