package org.statesync.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public abstract class ResponseMessage extends OutboundMessage {
	public final int forId;
	public final ResponseType type;
}
