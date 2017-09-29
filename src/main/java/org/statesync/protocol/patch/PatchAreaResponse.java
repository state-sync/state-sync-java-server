package org.statesync.protocol.patch;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatchAreaResponse extends ResponseMessage {
	public final String area;

	public PatchAreaResponse(final int forId, final String area) {
		super(forId, ResponseType.patchAreaSuccess);
		this.area = area;
	}
}
