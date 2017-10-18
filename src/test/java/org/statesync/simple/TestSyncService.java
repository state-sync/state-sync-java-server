package org.statesync.simple;

import org.statesync.SyncOutbound;
import org.statesync.SyncService;
import org.statesync.SyncServiceUser;

public class TestSyncService extends SyncService {
	private int sessionToken = 0;

	public TestSyncService(final SyncOutbound protocol) {
		super(protocol);
	}

	@Override
	protected String newSessionToken(final SyncServiceUser user) {
		return "user:" + this.sessionToken++;
	}
}
