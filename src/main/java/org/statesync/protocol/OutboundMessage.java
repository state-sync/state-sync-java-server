// Generated by delombok at Tue Jul 03 21:02:45 NOVT 2018
package org.statesync.protocol;

public abstract class OutboundMessage extends Message {
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "OutboundMessage()";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof OutboundMessage)) return false;
		final OutboundMessage other = (OutboundMessage) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof OutboundMessage;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		int result = super.hashCode();
		return result;
	}

	@java.lang.SuppressWarnings("all")
	public OutboundMessage() {
	}
}
