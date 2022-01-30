package org.statesync;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;

public class JsonSynchronizer<T>
{

	private ObjectMapper mapper;
	private Class<T> clazz;

	public JsonSynchronizer(final Class<T> clazz)
	{
		this.clazz = clazz;
		this.mapper = new ObjectMapper();
		this.mapper.findAndRegisterModules();
		// Redux and NgRx would like to have null value of property instead of
		// absent property
		this.mapper.setSerializationInclusion(Include.ALWAYS);
		// this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
		// false);
	}

	public T newModel()
	{
		try
		{
			return this.clazz.newInstance();
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public T clone(final T model)
	{
		return model(json(model));
	}

	public ArrayNode diff(final T original, final T updated)
	{
		return (ArrayNode) JsonDiff.asJson(json(original), json(updated));
	}

	public ArrayNode diff(final ObjectNode original, final ObjectNode updated)
	{
		return (ArrayNode) JsonDiff.asJson(original, updated);
	}

	public ObjectNode json(final T model)
	{
		return this.mapper.convertValue(model, ObjectNode.class);
	}

	public T model(final ObjectNode model)
	{
		try
		{
			return this.mapper.convertValue(model, this.clazz);
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public ObjectNode patch(final ObjectNode json, final ArrayNode patch)
	{
		try
		{
			return (ObjectNode) JsonPatch.fromJson(patch).apply(json);
		}
		catch (final JsonPatchException e)
		{
			// TODO: Phase 2 or remove completely
			e.printStackTrace();
			return json;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return json;
		}
	}

	public T patch(final T original, final ArrayNode patch)
	{
		try
		{
			return model((ObjectNode) JsonPatch.fromJson(patch).apply(json(original)));
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
