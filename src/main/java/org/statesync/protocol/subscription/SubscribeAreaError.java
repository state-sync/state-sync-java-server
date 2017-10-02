package org.statesync.protocol.subscription;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubscribeAreaError extends ResponseMessage {
	public final String area;
	public final AreaSubscriptionError error;

	public SubscribeAreaError(final int forId, final String area, final AreaSubscriptionError error) {
		super(forId, ResponseType.areaSubscriptionError);
		this.area = area;
		this.error = error;
	}
}
