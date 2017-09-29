package org.statesync.protocol.init;

import org.statesync.protocol.EventMessage;
import org.statesync.protocol.EventType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Initialize session
 *
 * @author ify
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InitSessionResponse extends EventMessage {
	public String sessionId;

	public InitSessionResponse(final String sessionId) {
		super(EventType.init);
		this.sessionId = sessionId;
	}
}
