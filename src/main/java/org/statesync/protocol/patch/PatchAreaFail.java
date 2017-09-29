package org.statesync.protocol.patch;

import org.statesync.protocol.ResponseMessage;
import org.statesync.protocol.ResponseType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatchAreaFail extends ResponseMessage {
	public final String area;
	public final PatchError error;

	public PatchAreaFail(final int forId, final String area, final PatchError error) {
		super(forId, ResponseType.patchAreaError);
		this.area = area;
		this.error = error;
	}
}
