package org.statesync;

public interface StateStorage<Model> {

	Model load(String key);

	void remove(String key);

	void save(Model model, String key);
}
