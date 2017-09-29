package org.statesync;

public interface StateProcessor<Model> {
	Model process(Model model, SyncAreaUser user);
}
