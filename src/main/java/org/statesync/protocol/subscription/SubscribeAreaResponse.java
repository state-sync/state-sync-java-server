package org.statesync.protocol.subscription;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribeAreaResponse extends ResponseMessage {
	public final String area;
	public final ClientAreaConfig config;
	public final ObjectNode model;

	public SubscribeAreaResponse(final int forId, final String area, final ClientAreaConfig config,
			final ObjectNode model) {
		super(forId, ResponseType.areaSubscription);
		this.area = area;
		this.config = config;
		this.model = model;
	}
}
