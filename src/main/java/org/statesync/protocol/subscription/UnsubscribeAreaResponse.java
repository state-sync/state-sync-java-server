
package org.statesync.protocol.subscription;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

public class UnsubscribeAreaResponse extends ResponseMessage
{
	public final String area;

	public UnsubscribeAreaResponse(final int forId, final String area)
	{
		super(forId, ResponseType.areaUnsubscription);
		this.area = area;
	}

	public String getArea()
	{
		return this.area;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "UnsubscribeAreaResponse(area=" + this.getArea() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof UnsubscribeAreaResponse)) return false;
		final UnsubscribeAreaResponse other = (UnsubscribeAreaResponse) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$area = this.getArea();
		final java.lang.Object other$area = other.getArea();
		if (this$area == null ? other$area != null : !this$area.equals(other$area)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof UnsubscribeAreaResponse;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $area = this.getArea();
		result = result * PRIME + ($area == null ? 43 : $area.hashCode());
		return result;
	}
}
