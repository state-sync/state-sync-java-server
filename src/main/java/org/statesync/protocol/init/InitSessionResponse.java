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
	/**
	 * Session token used by protocol, external sessionId can't be used because
	 * of security concerns
	 */
	public final String sessionToken;

	/**
	 * Protocol version
	 */
	public final String protocolVersion = "1.0";

	public InitSessionResponse(final String sessionToken) {
		super(EventType.init);
		this.sessionToken = sessionToken;
	}
}
