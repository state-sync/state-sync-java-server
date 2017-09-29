package org.statesync.protocol.subscription;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UnsubscribeAreaResponse extends ResponseMessage {
	public final String area;

	public UnsubscribeAreaResponse(final int forId, final String area) {
		super(forId, ResponseType.areaUnsubscriptionSuccess);
		this.area = area;
	}
}
