package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.statesync.config.ClientAreaConfig;
import org.statesync.config.SyncAreaConfig;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.singnal.SignalRequest;
import org.statesync.protocol.subscription.AreaSubscriptionError;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaResponse;

import com.google.common.base.Supplier;

import lombok.extern.java.Log;

@Log
public class SyncArea<Model> {
	private final Map<String, SyncAreaUser<Model>> users = new ConcurrentHashMap<>();
	private final Map<String, SyncAreaSession<Model>> sessions = new ConcurrentHashMap<>();

	SyncService service;
	private final String areaId;
	protected final JsonSynchronizer<Model> synchronizer;

	private SyncAreaConfig<Model> config;
	StateStorage userStorage;
	StateStorage sessionStorage;
	StateReducer<Model> processor;
	public Supplier<Model> factory;

	public SyncArea(final SyncAreaConfig<Model> config, final StateStorage userStorage,
			final StateStorage sessionStorage, final StateReducer<Model> processor) {
		this.config = config;
		this.userStorage = userStorage;
		this.sessionStorage = sessionStorage;
		this.processor = processor;
		this.areaId = config.getId();
		this.factory = () -> {
			try {
				return this.config.getModel().newInstance();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
		this.synchronizer = new JsonSynchronizer<>(config.getModel());
		log.info("Area initialized:" + this.config);
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	protected boolean checkPermissions(final String userId) {
		return true;
	}

	public final String getAreaId() {
		return this.areaId;
	}

	protected ClientAreaConfig getClientConfig(final SyncAreaUser<Model> user) {
		return this.config.getClientConfig();
	}

	public int getSessionsCount() {
		return this.sessions.size();
	}

	public int getUsersCount() {
		return this.users.size();
	}

	protected void onRegister(final SyncService service) {
		this.service = service;
	}

	public void patchArea(final SyncSession session, final PatchAreaRequest event) {
		this.users.get(session.user.userId).patch(session.sessionToken, event);
	}

	public void signal(final SyncSession session, final SignalRequest event) {
		this.users.get(session.user.userId).signal(session.sessionToken, event);
	}

	public void subscribeSession(final SyncSession session, final SubscribeAreaRequest event) {
		final String sessionToken = session.sessionToken;

		// security
		if (!checkPermissions(session.user.userId)) {
			this.service.protocol.sendSubscribeAreaFail(sessionToken, event.id, this.areaId,
					AreaSubscriptionError.accessDenied);
			return;
		}

		if (this.sessions.containsKey(sessionToken)) {
			this.service.protocol.sendSubscribeAreaFail(sessionToken, event.id, this.areaId,
					AreaSubscriptionError.alreadySubscribed);
			return;
		}

		// link
		final SyncAreaUser<Model> areaUser = this.users.computeIfAbsent(session.user.userId,
				id -> new SyncAreaUser<>(session.user, this));
		final SyncAreaSession<Model> areaSession = new SyncAreaSession<Model>(session, areaUser);

		areaSession.subscribe(event);
	}

	public void syncAll() {
		syncAll(null);
	}

	public void syncAll(final StateReducer<Model> synchronizer) {
		this.users.forEach((k, user) -> {
			user.sync(synchronizer);
		});
	}

	public void unregister() {
		this.service = null;
	}

	public void unsubscribeSession(final SyncSession session, final UnsubscribeAreaRequest event) {

		// unlink
		this.sessions.remove(session.sessionToken);
		this.users.computeIfPresent(session.user.userId, (id, user) -> user.remove(session) ? null : user);

		// response
		final UnsubscribeAreaResponse response = new UnsubscribeAreaResponse(event.id, this.areaId);
		this.service.protocol.send(session.sessionToken, response);
	}
}
