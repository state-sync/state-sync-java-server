
package org.statesync.protocol.patch;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PatchAreaRequest extends RequestMessage
{
	public ArrayNode patch;

	public PatchAreaRequest(final int id, final String area, final ArrayNode patch)
	{
		super(id, RequestType.p, area);
		this.patch = patch;
	}

	public ArrayNode getPatch()
	{
		return this.patch;
	}

	public void setPatch(final ArrayNode patch)
	{
		this.patch = patch;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "PatchAreaRequest(patch=" + this.getPatch() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof PatchAreaRequest)) return false;
		final PatchAreaRequest other = (PatchAreaRequest) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$patch = this.getPatch();
		final java.lang.Object other$patch = other.getPatch();
		if (this$patch == null ? other$patch != null : !this$patch.equals(other$patch)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof PatchAreaRequest;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $patch = this.getPatch();
		result = result * PRIME + ($patch == null ? 43 : $patch.hashCode());
		return result;
	}

	public PatchAreaRequest()
	{
	}
}
