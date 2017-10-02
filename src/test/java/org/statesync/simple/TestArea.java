package org.statesync.simple;

import org.statesync.InMemoryStateStorage;
import org.statesync.SyncArea;
import org.statesync.SyncAreaUser;
import org.statesync.config.SyncAreaConfig;

public class TestArea extends SyncArea<TestModel> {
	static final SyncAreaConfig<TestModel> config = new SyncAreaConfig<>();
	static InMemoryStateStorage<TestModel> sessionStorage = new InMemoryStateStorage<>(TestModel::new);
	static InMemoryStateStorage<TestModel> userStorage = new InMemoryStateStorage<>(TestModel::new);
	static {
		config.setId("test");
		config.setModel(TestModel.class);
	}

	protected static TestModel process(final TestModel model, final SyncAreaUser<TestModel> user) {
		return model;
	}

	public TestArea() {
		super(config, userStorage, sessionStorage, TestArea::process);
	}
}
