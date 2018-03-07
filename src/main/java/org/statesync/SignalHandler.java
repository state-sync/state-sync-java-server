package org.statesync;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Generic interface of signal handler. Implementation may return same object.
 * Immutability is not required.
 *
 * @author ify
 *
 * @param <Model>
 */
@FunctionalInterface
public interface SignalHandler<Model> {

	/**
	 * Handle signal.
	 *
	 * @param model
	 *            - current model
	 * @param user
	 *            - user
	 * @param signal
	 *            - signal name
	 * @param parameters
	 *            - signal parameters
	 * @return updated model
	 */
	Model handle(Model model, SyncAreaApi<Model> user, String signal, ObjectNode parameters);

}
