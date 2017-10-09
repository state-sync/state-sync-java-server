package org.statesync.protocol.signal;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignalResponse extends ResponseMessage {
	public final String area;

	public SignalResponse(final int forId, final String area) {
		super(forId, ResponseType.signalResponse);
		this.area = area;
	}
}
