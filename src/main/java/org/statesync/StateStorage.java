package org.statesync;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * State storage, stores state model in form of JSON.
 *
 * @author ify
 *
 */
public interface StateStorage {

	/**
	 * Loads model by key
	 *
	 * @param key
	 * @return model in json form or null
	 */
	ObjectNode load(String key);

	/**
	 * Removes model by key
	 *
	 * @param key
	 */
	void remove(String key);

	/**
	 * Stores model by key
	 * 
	 * @param key
	 *            - model key
	 * @param json
	 *            - model json
	 */
	void save(String key, ObjectNode json);
}
