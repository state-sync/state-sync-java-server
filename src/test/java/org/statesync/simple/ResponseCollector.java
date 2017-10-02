package org.statesync.simple;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.statesync.SyncOutbound;
import org.statesync.protocol.Message;

public class ResponseCollector implements SyncOutbound {

	private final List<Pair<String, Message>> events = new ArrayList<>();

	@Override
	public void send(final String sessionToken, final Message event) {
		this.events.add(Pair.of("session:" + sessionToken, event));
	}

}
