package org.statesync;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;

public class JsonSynchronizer<T> {

	private ObjectMapper mapper;
	private Class<T> clazz;

	public JsonSynchronizer(final Class<T> clazz) {
		this.clazz = clazz;
		this.mapper = new ObjectMapper();
		// Redux and NgRx would like to have null value of property instead of
		// absent property
		this.mapper.setSerializationInclusion(Include.ALWAYS);
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
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

	public ObjectNode patch(final ObjectNode json, final ArrayNode patch) {
		try {
			return (ObjectNode) JsonPatch.fromJson(patch).apply(json);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public T patch(final T original, final ArrayNode patch) {
		try {
			return model((ObjectNode) JsonPatch.fromJson(patch).apply(json(original)));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
