package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.java.Log;

@Log
public class SyncAreaUser<Model> {
	public final Map<String, SyncAreaSession<Model>> sessions = new ConcurrentHashMap<>();

	private StateStorage userStorage;

	private StateReducer<Model> processor;

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

	public Model load() {
		final String userId = this.user.userId;
		synchronized (this.userLock) {
			final ObjectNode json = this.userStorage.load(userId);
			if (json == null) {
				final Model original = this.processor.reduce(this.area.factory.get(), this);
				final ObjectNode updatedJson = this.synchronizer.json(original);
				this.userStorage.save(this.user.userId, updatedJson);
				return original;
			} else {
				return this.synchronizer.model(json);
			}
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
			return this.area.handleSignal(model, user, event.signal, event.parameters);
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
	public void sync(final StateReducer<Model> enchancer) {
		synchronized (this.userLock) {
			try {
				// load model
				final Model original = load();
				Model updated = this.synchronizer.clone(original);

				// apply client patch
				if (enchancer != null) {
					// updated = this.processor.process(updated, this);
					updated = enchancer.reduce(updated, this);
				}
				updated = this.processor.reduce(updated, this);

				// save if required
				final ArrayNode spatch = this.synchronizer.diff(original, updated);
				if (spatch.size() > 0) {
					this.userStorage.save(this.user.userId, this.synchronizer.json(updated));
				}
				final Model nv = updated;
				this.sessions.values().forEach(session -> session.onChange(nv));
			} catch (final Exception e) {
				log.log(Level.SEVERE, "sync failed", e);
			}
		}
	}
}
