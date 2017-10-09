package org.statesync;

public interface SyncServiceVisitor {

	<T> void visit(JsonSynchronizer<T> jsonSynchronizer);

	void visit(SessionMap sessionMap);

	<T> void visit(SyncArea<T> syncArea);

	<T> void visit(SyncAreaSession<T> syncAreaSession);

	<T> void visit(SyncAreaUser<T> syncAreaUser);

	void visit(SyncService syncService);

	void visit(SyncServiceUser syncServiceUser);

	void visit(SyncServiceSession syncSession);

}
