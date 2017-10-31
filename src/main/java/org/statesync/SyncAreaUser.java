package org.statesync;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

	private final Set<Dependency> dependencies = new HashSet<>();

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
		this.processor = area.reducer;
		this.protocol = area.service.protocol;
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public ClientAreaConfig getClientConfig() {
		return this.area.getClientConfig(this);
	}

	public String getUserId() {
		return this.user.userId;
	}

	public Model load() {
		final String userId = this.user.userId;
		synchronized (this.userLock) {
			final ObjectNode json = this.userStorage.load(userId);
			if (json == null) {
				this.cleanDependencies();
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
		this.sessions.get(sessionToken).patch(event);
		patch(event.patch);
		this.protocol.confirmPatch(sessionToken, event);
	}

	public boolean removeSession(final String sessionToken) {
		this.sessions.remove(sessionToken);
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
				this.cleanDependencies();
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

	public void cleanDependencies() {
		this.dependencies.clear();

	}

	public void listen(final Dependency... dependency) {
		for (final Dependency d : dependency) {
			this.dependencies.add(d);
		}
	}

	public void listenForUser(final Dependency... dependency) {
		for (final Dependency d : dependency) {
			this.dependencies.add(d.child(getUserId()));
		}
	}

	public void fire(final Dependency dependency) {
		this.area.fireChanges(dependency, this);
	}

	public boolean hasDependency(final Dependency dependency) {
		return this.dependencies.contains(dependency);
	}

	public void fireForUser(final Dependency dependency) {
		fire(dependency.child(this.getUserId()));
	}
}
