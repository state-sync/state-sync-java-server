package org.statesync.protocol.sync;

import org.statesync.protocol.EventMessage;
import org.statesync.protocol.EventType;

import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatchAreaEvent extends EventMessage {
	public String area;
	public ArrayNode patch;

	public PatchAreaEvent(final String area, final ArrayNode patch) {
		super(EventType.p);
		this.area = area;
		this.patch = patch;
	}
}
