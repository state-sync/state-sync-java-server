
package org.statesync.protocol;

public abstract class OutboundMessage extends Message
{
	@java.lang.Override

	public java.lang.String toString()
	{
		return "OutboundMessage()";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof OutboundMessage)) return false;
		final OutboundMessage other = (OutboundMessage) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof OutboundMessage;
	}

	@java.lang.Override

	public int hashCode()
	{
		int result = super.hashCode();
		return result;
	}

	public OutboundMessage()
	{
	}
}
