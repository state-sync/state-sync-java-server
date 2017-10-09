package org.statesync.protocol;

import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestMessageFactory {
	private final ObjectMapper mapper = new ObjectMapper();

	public RequestMessage parse(final JsonNode json) {
		final RequestType type = RequestType.valueOf(json.get("type").asText());
		switch (type) {
			case subscribeArea:
				return this.mapper.convertValue(json, SubscribeAreaRequest.class);
			case unsubscribeArea:
				return this.mapper.convertValue(json, UnsubscribeAreaRequest.class);
			case signal:
				return this.mapper.convertValue(json, SignalRequest.class);
			case p:
			default:
				return this.mapper.convertValue(json, PatchAreaRequest.class);
		}

	}
}
