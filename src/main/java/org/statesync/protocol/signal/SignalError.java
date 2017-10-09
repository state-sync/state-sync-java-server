package org.statesync.protocol.signal;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SignalError extends ResponseMessage {
	public final String area;
	public final SignalErrorCode error;

	public SignalError(final int forId, final String area, final SignalErrorCode error) {
		super(forId, ResponseType.signalError);
		this.area = area;
		this.error = error;
	}
}
