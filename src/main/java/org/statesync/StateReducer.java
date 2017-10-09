package org.statesync;

/**
 * Generic interface of state reducer. Implementation may return same object.
 * Immutability is not required.
 *
 * @author ify
 *
 * @param <Model>
 */
@FunctionalInterface
public interface StateReducer<Model> {
	/**
	 * Reduce model
	 *
	 * @param model
	 *            - model to reduce
	 * @param user
	 *            - user of sync ares
	 * @return changed model
	 */
	Model reduce(Model model, SyncAreaUser<Model> user);
}
