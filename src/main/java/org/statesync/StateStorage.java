package org.statesync;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface StateStorage {

	ObjectNode load(String key);

	void remove(String key);

	void save(String key, ObjectNode json);
}
