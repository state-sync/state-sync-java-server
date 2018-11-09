
package org.statesync;

public final class ComplexDependency implements Dependency
{
	public final Dependency root;
	public final String key;

	public ComplexDependency(final Dependency root, final String key)
	{
		this.root = root;
		this.key = key;
	}

	public Dependency getRoot()
	{
		return root;
	}

	public String getKey()
	{
		return key;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ComplexDependency other = (ComplexDependency) obj;
		if (key == null)
		{
			if (other.key != null) return false;
		}
		else if (!key.equals(other.key)) return false;
		if (root == null)
		{
			if (other.root != null) return false;
		}
		else if (!root.equals(other.root)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ComplexDependency [root=" + root + ", key=" + key + "]";
	}
}
