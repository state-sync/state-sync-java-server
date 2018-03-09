package org.statesync.protocol.patch;

import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.RequestType;

import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PatchAreaRequest extends RequestMessage {
	public ArrayNode patch;

	public PatchAreaRequest(final int id, final String area, final ArrayNode patch) {
		super(id, RequestType.p, area);
		this.patch = patch;
	}
}
