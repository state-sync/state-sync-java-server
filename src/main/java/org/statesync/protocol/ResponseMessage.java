
package org.statesync.protocol;

public abstract class ResponseMessage extends OutboundMessage
{
	public final int forId;
	public final ResponseType type;

	public int getForId()
	{
		return this.forId;
	}

	public ResponseType getType()
	{
		return this.type;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "ResponseMessage(forId=" + this.getForId() + ", type=" + this.getType() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof ResponseMessage)) return false;
		final ResponseMessage other = (ResponseMessage) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		if (this.getForId() != other.getForId()) return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof ResponseMessage;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		result = result * PRIME + this.getForId();
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 43 : $type.hashCode());
		return result;
	}

	public ResponseMessage(final int forId, final ResponseType type)
	{
		this.forId = forId;
		this.type = type;
	}
}
