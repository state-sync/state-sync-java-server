package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.singnal.SignalRequest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SyncAreaUser<Model> {
	public final Map<String, SyncAreaSession<Model>> sessions = new ConcurrentHashMap<>();

	private StateStorage<Model> userStorage;

	private StateProcessor<Model> processor;

	private final Object userLock = new Object();

	private SyncServiceUser user;

	private JsonSynchronizer<Model> synchronizer;

	SyncArea<Model> area;

	private SyncOutbound protocol;

	public SyncAreaUser(final SyncServiceUser user, final SyncArea<Model> area) {
		super();
		this.user = user;
		this.area = area;
		this.userStorage = area.userStorage;
		this.synchronizer = area.synchronizer;
		this.processor = area.processor;
		this.protocol = area.service.protocol;
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public ClientAreaConfig getClientConfig() {
		return this.area.getClientConfig(this);
	}

	protected Model handleSignal(final Model model, final SyncAreaUser<Model> user, final String signal,
			final ObjectNode parameters) {
		return model;
	}

	public Model load() {
		final String userId = this.user.userId;
		synchronized (this.userLock) {
			final Model original = this.userStorage.load(userId);
			Model updated = this.synchronizer.clone(original);
			updated = this.processor.process(updated, this);
			final ArrayNode patch = this.synchronizer.diff(original, updated);
			if (patch.size() > 0) {
				this.userStorage.save(updated, this.user.userId);
			}
			return updated;
		}
	}

	public void patch(final ArrayNode patch) {
		sync((model, user) -> {
			return this.synchronizer.patch(model, patch);
		});
	}

	public void patch(final String sessionToken, final PatchAreaRequest event) {
		patch(event.patch);
		this.protocol.confirmPatch(sessionToken, event);

	}

	public boolean remove(final SyncSession session) {
		this.sessions.remove(session.sessionToken);
		return this.sessions.isEmpty();
	}

	public void signal(final String sessionToken, final SignalRequest event) {
		sync((model, user) -> {
			return handleSignal(model, user, event.signal, event.parameters);
		});
		this.protocol.confirmSignal(sessionToken, event);
	}

	/**
	 * Sync model
	 * <ul>
	 * <li>Load or create model</li>
	 * <li>Change model by provided enchancer</li>
	 * </ul>
	 *
	 * @param enchancer
	 */
	public void sync(final StateProcessor<Model> enchancer) {
		synchronized (this.userLock) {
			// load model
			final Model original = this.userStorage.load(this.user.userId);
			Model updated = this.synchronizer.clone(original);

			// apply client patch
			if (enchancer != null) {
				updated = this.processor.process(updated, this);
				updated = enchancer.process(updated, this);
			}
			updated = this.processor.process(updated, this);

			// save if required
			final ArrayNode spatch = this.synchronizer.diff(original, updated);
			if (spatch.size() > 0) {
				this.userStorage.save(updated, this.user.userId);
			}
			final Model nv = updated;
			this.sessions.values().forEach(session -> session.onChange(nv));
		}
	}
}
