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

	Model handle(Model model, SyncAreaUser<Model> user, String signal, ObjectNode parameters);

}
