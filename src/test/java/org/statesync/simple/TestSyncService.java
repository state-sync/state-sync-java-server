package org.statesync.simple;

import org.statesync.SyncOutbound;
import org.statesync.SyncService;

public class TestSyncService extends SyncService {
	private int sessionToken = 0;
	private int userToken = 0;

	public TestSyncService(final SyncOutbound protocol) {
		super(protocol);
	}

	@Override
	protected String newSessionToken(final String userId) {
		return "user:" + this.sessionToken++;
	}

	@Override
	protected String newUserToken(final String userId) {
		return "session:" + this.userToken++;
	}
}
