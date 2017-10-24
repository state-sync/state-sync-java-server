package org.statesync.simple;

import org.statesync.SyncOutbound;
import org.statesync.SyncService;

public class TestSyncService extends SyncService {
	private int sessionToken = 0;

	public TestSyncService(final SyncOutbound protocol) {
		super(protocol);
	}
}
