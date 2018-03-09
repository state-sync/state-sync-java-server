package org.statesync.protocol.subscription;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UnsubscribeAreaRequest extends RequestMessage {

	public UnsubscribeAreaRequest(final int id, final String area) {
		super(id, RequestType.unsubscribeArea, area);
	}
}
