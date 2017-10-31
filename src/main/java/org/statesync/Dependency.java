package org.statesync;

public interface Dependency {
	@Override
	boolean equals(Object obj);

	public default Dependency child(final String key) {
		return new ComplexDependency(this, key);
	}
}
