
package org.statesync.protocol;

public abstract class RequestMessage extends Message
{
	public int id;
	public RequestType type;
	public String area;

	public int getId()
	{
		return this.id;
	}

	public RequestType getType()
	{
		return this.type;
	}

	public String getArea()
	{
		return this.area;
	}

	public void setId(final int id)
	{
		this.id = id;
	}

	public void setType(final RequestType type)
	{
		this.type = type;
	}

	public void setArea(final String area)
	{
		this.area = area;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "RequestMessage(id=" + this.getId() + ", type=" + this.getType() + ", area=" + this.getArea() + ")";
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof RequestMessage)) return false;
		final RequestMessage other = (RequestMessage) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (!super.equals(o)) return false;
		if (this.getId() != other.getId()) return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
		final java.lang.Object this$area = this.getArea();
		final java.lang.Object other$area = other.getArea();
		if (this$area == null ? other$area != null : !this$area.equals(other$area)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof RequestMessage;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = super.hashCode();
		result = result * PRIME + this.getId();
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 43 : $type.hashCode());
		final java.lang.Object $area = this.getArea();
		result = result * PRIME + ($area == null ? 43 : $area.hashCode());
		return result;
	}

	public RequestMessage()
	{
	}

	public RequestMessage(final int id, final RequestType type, final String area)
	{
		this.id = id;
		this.type = type;
		this.area = area;
	}
}
