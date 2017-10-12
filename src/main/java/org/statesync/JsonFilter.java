package org.statesync;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonFilter {

	private static final JsonNodeFactory jsonFactory = JsonNodeFactory.instance;
	private String localPrefix;
	private Set<String> serverPush;
	private boolean syncAll;
	private Set<String> topProperties;

	public JsonFilter(final String localPrefix, final String[] serverPush) {
		this.localPrefix = "/" + localPrefix;
		this.serverPush = new HashSet<>(Arrays.asList(serverPush));
		this.topProperties = this.serverPush.stream().map(p -> p.substring(1)).collect(Collectors.toSet());
		this.syncAll = serverPush.length == 1 && serverPush[0].equals("/");
	}

	private void filterArray(final ArrayNode arr) {
		for (final JsonNode child : arr) {
			filterNode(child);
		}
	}

	public ObjectNode filterModel(final ObjectNode json) {
		if (!this.syncAll) {
			for (final Iterator<Map.Entry<String, JsonNode>> it = json.fields(); it.hasNext();) {
				final Entry<String, JsonNode> entry = it.next();
				final String name = entry.getKey();
				if (!this.topProperties.contains(name)) {
					it.remove();
				}
			}
		}
		filterObject(json);
		return json;
	}

	private void filterNode(final JsonNode node) {
		if (node instanceof ObjectNode) {
			filterObject((ObjectNode) node);
		} else if (node instanceof ArrayNode) {
			filterArray((ArrayNode) node);
		}
	}

	private void filterObject(final ObjectNode json) {
		for (final Iterator<Map.Entry<String, JsonNode>> it = json.fields(); it.hasNext();) {
			final Entry<String, JsonNode> entry = it.next();
			final String name = entry.getKey();
			if (name.startsWith(this.localPrefix)) {
				it.remove();
			} else {
				filterNode(entry.getValue());
			}
		}
	}

	public ArrayNode filterPatch(final ArrayNode patch) {
		final ArrayNode res = jsonFactory.arrayNode(patch.size());
		CHECK: for (final JsonNode _child : patch) {
			final ObjectNode child = (ObjectNode) _child;
			final String path = child.get("path").asText();
			if (path.contains(this.localPrefix))
				continue CHECK;

			if (!this.syncAll)
				for (final String sp : this.serverPush) {
					if (!path.startsWith(sp))
						continue CHECK;
				}

			res.add(child);
		}
		return res;
	}

}
