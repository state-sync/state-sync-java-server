
package org.statesync.protocol;

public abstract class EventMessage extends OutboundMessage
{
	public final EventType type;

	public EventType getType()
	{
		return this.type;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "EventMessage(type=" + this.getType() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof EventMessage)) return false;
		final EventMessage other = (EventMessage) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof EventMessage;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 43 : $type.hashCode());
		return result;
	}

	public EventMessage(final EventType type)
	{
		this.type = type;
	}
}
