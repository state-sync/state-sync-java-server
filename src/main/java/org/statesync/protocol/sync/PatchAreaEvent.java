
package org.statesync.protocol.sync;

import org.statesync.protocol.EventMessage;
import org.statesync.protocol.EventType;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PatchAreaEvent extends EventMessage
{
	public String area;
	public ArrayNode patch;

	public PatchAreaEvent(final String area, final ArrayNode patch)
	{
		super(EventType.p);
		this.area = area;
		this.patch = patch;
	}

	public String getArea()
	{
		return this.area;
	}

	public ArrayNode getPatch()
	{
		return this.patch;
	}

	public void setArea(final String area)
	{
		this.area = area;
	}

	public void setPatch(final ArrayNode patch)
	{
		this.patch = patch;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "PatchAreaEvent(area=" + this.getArea() + ", patch=" + this.getPatch() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof PatchAreaEvent)) return false;
		final PatchAreaEvent other = (PatchAreaEvent) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$area = this.getArea();
		final java.lang.Object other$area = other.getArea();
		if (this$area == null ? other$area != null : !this$area.equals(other$area)) return false;
		final java.lang.Object this$patch = this.getPatch();
		final java.lang.Object other$patch = other.getPatch();
		if (this$patch == null ? other$patch != null : !this$patch.equals(other$patch)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof PatchAreaEvent;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $area = this.getArea();
		result = result * PRIME + ($area == null ? 43 : $area.hashCode());
		final java.lang.Object $patch = this.getPatch();
		result = result * PRIME + ($patch == null ? 43 : $patch.hashCode());
		return result;
	}
}
