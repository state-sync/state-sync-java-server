package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.base.Supplier;

public class InMemoryStateStorage<Model> implements StateStorage<Model> {

	private final Map<String, Model> models = new ConcurrentHashMap<>();
	private Supplier<Model> factory;

	public InMemoryStateStorage(final Supplier<Model> factory) {
		this.factory = factory;
	}

	public Model byUser(final String userId) {
		return this.models.get(userId);
	}

	@Override
	public Model load(final SyncAreaUser user) {
		return this.models.computeIfAbsent(user.userId, id -> this.factory.get());
	}

	@Override
	public void save(final Model model, final SyncAreaUser user) {
		this.models.put(user.userId, model);
	}

}
