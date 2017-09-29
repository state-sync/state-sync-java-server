package org.statesync;

import java.io.IOException;

import org.statesync.config.SyncAreaConfig;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;

public class JsonSynchronizer<T> {

	private ObjectMapper mapper;
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public JsonSynchronizer(final SyncAreaConfig config) {
		this.clazz = (Class<T>) config.getModel();
		this.mapper = new ObjectMapper();
		// Redux and NgRx would like to have null value of property instead of
		// absent property
		this.mapper.setSerializationInclusion(Include.ALWAYS);
	}

	public T clone(final T model) {
		return model(json(model));
	}

	public ArrayNode diff(final T original, final T updated) {
		return (ArrayNode) JsonDiff.asJson(json(original), json(updated));
	}

	public ObjectNode json(final T model) {
		return this.mapper.valueToTree(model);
	}

	public T model(final ObjectNode model) {
		return this.mapper.convertValue(model, this.clazz);
	}

	public T patch(final T original, final JsonNode patch) throws JsonPatchException, IOException {
		return model((ObjectNode) JsonPatch.fromJson(patch).apply(json(original)));
	}
}
