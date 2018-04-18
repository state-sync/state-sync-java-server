package org.statesync.simple;

import org.statesync.SyncOutbound;
import org.statesync.SyncService;

public class TestSyncService extends SyncService {

	public TestSyncService(final SyncOutbound protocol) {
		super(protocol, null, 1);
	}
}
