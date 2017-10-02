package org.statesync;

public class SyncServiceVisitorBase implements SyncServiceVisitor {

	@Override
	public <T> void visit(final JsonSynchronizer<T> jsonSynchronizer) {
		// override
	}

	@Override
	public void visit(final SessionMap sessionMap) {
		sessionMap.getSessions().forEach(session -> session.accept(this));
	}

	@Override
	public <T> void visit(final SyncArea<T> syncArea) {
		syncArea.synchronizer.accept(this);
	}

	@Override
	public <T> void visit(final SyncAreaSession<T> syncAreaSession) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void visit(final SyncAreaUser<T> syncAreaUser) {
		// override
		syncAreaUser.sessions.values().forEach(session -> session.accept(this));
	}

	@Override
	public void visit(final SyncService syncService) {
		syncService.areas.values().forEach(area -> area.accept(this));
		syncService.users.values().forEach(user -> user.accept(this));
		syncService.sessions.getSessions().forEach(session -> session.accept(this));
	}

	@Override
	public void visit(final SyncServiceUser syncServiceUser) {
		// override
	}

	@Override
	public void visit(final SyncSession syncSession) {
		// override
	}

}
