package org.statesync.protocol.signal;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SignalRequest extends RequestMessage {
	public String signal;
	public ObjectNode parameters;

	public SignalRequest(final int id, final String area, final String signal, final ObjectNode parameters) {
		super(id, RequestType.signal, area);
		this.signal = signal;
		this.parameters = parameters;
	}
}
