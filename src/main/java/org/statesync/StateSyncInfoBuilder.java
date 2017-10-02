package org.statesync;

import java.util.ArrayList;

import org.statesync.info.StateSyncInfo;

public class StateSyncInfoBuilder extends SyncServiceVisitorBase {
	public final StateSyncInfo model = new StateSyncInfo();

	@Override
	public void visit(final SyncService syncService) {
		this.model.userIds = new ArrayList<String>(syncService.users.keySet());
		super.visit(syncService);
	}

	@Override
	public void visit(final SyncSession syncSession) {
		this.model.sessions.add(syncSession.getInfo());
		super.visit(syncSession);
	}
}
