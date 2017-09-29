package org.statesync.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public abstract class EventMessage extends OutboundMessage {
	public final EventType type;
}
