
package org.statesync.info;

public class SyncAreaInfo
{
	String id;

	public String getId()
	{
		return this.id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	@java.lang.Override

	public boolean equals(final java.lang.Object o)
	{
		if (o == this) return true;
		if (!(o instanceof SyncAreaInfo)) return false;
		final SyncAreaInfo other = (SyncAreaInfo) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		return true;
	}

	protected boolean canEqual(final java.lang.Object other)
	{
		return other instanceof SyncAreaInfo;
	}

	@java.lang.Override

	public int hashCode()
	{
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	@java.lang.Override

	public java.lang.String toString()
	{
		return "SyncAreaInfo(id=" + this.getId() + ")";
	}

	public SyncAreaInfo(final String id)
	{
		this.id = id;
	}
}
