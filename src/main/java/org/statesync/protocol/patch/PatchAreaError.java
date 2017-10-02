package org.statesync.protocol.patch;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatchAreaError extends ResponseMessage {
	public final String area;
	public final PatchErrorCode error;

	public PatchAreaError(final int forId, final String area, final PatchErrorCode error) {
		super(forId, ResponseType.patchAreaError);
		this.area = area;
		this.error = error;
	}
}
