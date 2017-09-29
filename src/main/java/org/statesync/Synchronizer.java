package org.statesync;

@FunctionalInterface
public interface Synchronizer<Model> {
	public boolean sync(Model model, SyncSession session);
}
