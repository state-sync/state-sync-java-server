package org.statesync.simple;

import org.statesync.InMemoryStateStorage;
import org.statesync.SyncArea;
import org.statesync.SyncAreaUser;
import org.statesync.config.SyncAreaConfig;

public class TestArea extends SyncArea<TestModel> {
	static final SyncAreaConfig<TestModel> config = new SyncAreaConfig<>();
	static InMemoryStateStorage<TestModel> store = new InMemoryStateStorage<>(TestModel::new);
	static {
		config.setId("test");
		config.setModel(TestModel.class);
	}

	protected static TestModel process(final TestModel model, final SyncAreaUser user) {
		return model;
	}

	public TestArea() {
		super(config, store, TestArea::process);
	}
}
