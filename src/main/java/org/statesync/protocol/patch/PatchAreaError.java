
package org.statesync.protocol.patch;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

public class PatchAreaError extends ResponseMessage
{
	public final String area;
	public final PatchErrorCode error;

	public PatchAreaError(final int forId, final String area, final PatchErrorCode error)
	{
		super(forId, ResponseType.patchAreaError);
		this.area = area;
		this.error = error;
	}

	public String getArea()
	{
		return this.area;
	}

	public PatchErrorCode getError()
	{
		return this.error;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "PatchAreaError(area=" + this.getArea() + ", error=" + this.getError() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof PatchAreaError)) return false;
		final PatchAreaError other = (PatchAreaError) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$area = this.getArea();
		final java.lang.Object other$area = other.getArea();
		if (this$area == null ? other$area != null : !this$area.equals(other$area)) return false;
		final java.lang.Object this$error = this.getError();
		final java.lang.Object other$error = other.getError();
		if (this$error == null ? other$error != null : !this$error.equals(other$error)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof PatchAreaError;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $area = this.getArea();
		result = result * PRIME + ($area == null ? 43 : $area.hashCode());
		final java.lang.Object $error = this.getError();
		result = result * PRIME + ($error == null ? 43 : $error.hashCode());
		return result;
	}
}
