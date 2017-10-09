package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class InMemoryStateStorage implements StateStorage {

	private final Map<String, ObjectNode> models = new ConcurrentHashMap<>();

	@Override
	public ObjectNode load(final String key) {
		return this.models.get(key);
	}

	@Override
	public void remove(final String key) {
		this.models.remove(key);
	}

	@Override
	public void save(final String key, final ObjectNode json) {
		this.models.put(key, json);
	}

}
