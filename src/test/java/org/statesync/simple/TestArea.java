package org.statesync.simple;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.statesync.SyncArea;
import org.statesync.SyncAreaUser;
import org.statesync.config.ClientAreaConfig;
import org.statesync.config.SyncAreaConfig;

public class TestArea extends SyncArea<TestModel> {

	static SyncAreaConfig cfg = new SyncAreaConfig();
	static {
		cfg.setId("test");
		cfg.setModel(TestModel.class);
	}
	public final Map<String, TestModel> store = new ConcurrentHashMap<>();

	public TestArea() {
		super(cfg);
	}

	@Override
	protected ClientAreaConfig getConfig(final SyncAreaUser user) {
		return new ClientAreaConfig();
	}

	@Override
	protected TestModel loadModel(final SyncAreaUser user) {
		return this.store.computeIfAbsent(user.userId, id -> new TestModel());
	}

	@Override
	protected TestModel process(final TestModel model, final SyncAreaUser user) {
		return model;
	}

	@Override
	protected void storeModel(final TestModel model, final SyncAreaUser user) {
		this.store.put(user.userId, model);
	}

}
