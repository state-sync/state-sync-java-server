package org.statesync;

import lombok.Data;

final @Data public class ComplexDependency implements Dependency {
	public final Dependency root;
	public final String key;
}
