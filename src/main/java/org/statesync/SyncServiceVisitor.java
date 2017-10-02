package org.statesync;

public interface SyncServiceVisitor {

	<T> void visit(JsonSynchronizer<T> jsonSynchronizer);

	void visit(SessionMap sessionMap);

	<T> void visit(SyncArea<T> syncArea);

	void visit(SyncAreaUser syncAreaUser);

	void visit(SyncService syncService);

	void visit(SyncServiceUser syncServiceUser);

	void visit(SyncSession syncSession);

}
