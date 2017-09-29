package org.statesync;

public interface StateStorage<Model> {

	Model load(SyncAreaUser user);

	void save(Model model, SyncAreaUser user);

}
