package org.statesync.protocol.subscription;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SubscribeAreaRequest extends RequestMessage {
	public SubscribeAreaRequest(final int id, final String area) {
		super(id, RequestType.subscribeArea, area);
	}
}
