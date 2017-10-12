package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.statesync.config.ClientAreaConfig;
import org.statesync.config.SyncAreaConfig;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.AreaSubscriptionError;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Supplier;

import lombok.extern.java.Log;

/**
 * Sync area. Process sync between server model and user sessions.
 *
 * @author ify
 *
 * @param <Model>
 */
@Log
public class SyncArea<Model> {
	/**
	 * User subscribed to area. Each user use own copy of area model.
	 */
	private final Map<String, SyncAreaUser<Model>> users = new ConcurrentHashMap<>();
	/**
	 * User sessions subscribed to area.
	 */
	private final Map<String, SyncAreaSession<Model>> sessions = new ConcurrentHashMap<>();

	/**
	 * Sync service reference
	 */
	SyncService service;
	/**
	 * Unique area id
	 */
	private final String areaId;
	/**
	 * Synchronizer of model
	 */
	protected final JsonSynchronizer<Model> synchronizer;

	/**
	 * Area configuration
	 */
	private SyncAreaConfig<Model> config;
	/**
	 * User storage
	 */
	final StateStorage userStorage;
	/**
	 * Session storage
	 */
	final StateStorage sessionStorage;
	/**
	 * Model reducer
	 */
	final StateReducer<Model> reducer;
	/**
	 * New model factory
	 */
	final Supplier<Model> factory;
	/**
	 * Signal handler
	 */
	private final SignalHandler<Model> signalHandler;
	/**
	 * Output filter
	 */
	final JsonFilter jsonFilter;

	/**
	 * Construct new sync area.
	 *
	 * @param config
	 *            - area configuration
	 * @param userStorage
	 *            - user data storage
	 * @param sessionStorage
	 *            - session data storage
	 * @param reducer
	 *            - model reducer
	 * @param signalHandler
	 *            - signal handler
	 */
	public SyncArea(final SyncAreaConfig<Model> config, final StateStorage userStorage,
			final StateStorage sessionStorage, final StateReducer<Model> reducer,
			final SignalHandler<Model> signalHandler) {
		this.config = config;
		this.jsonFilter = new JsonFilter(config.getServerLocalPrefix(), config.getServerPush());
		this.userStorage = userStorage;
		this.sessionStorage = sessionStorage;
		this.reducer = reducer;
		this.signalHandler = signalHandler;
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

	public SyncAreaConfig<Model> getConfig() {
		return this.config;
	}

	public int getSessionsCount() {
		return this.sessions.size();
	}

	public int getUsersCount() {
		return this.users.size();
	}

	public Model handleSignal(final Model model, final SyncAreaUser<Model> user, final String signal,
			final ObjectNode parameters) {
		return this.signalHandler.handle(model, user, signal, parameters);
	}

	protected void onRegister(final SyncService service) {
		this.service = service;
	}

	public void patchArea(final SyncServiceSession session, final PatchAreaRequest event) {
		this.users.get(session.user.userId).patch(session.sessionToken, event);
	}

	public void signal(final SyncServiceSession session, final SignalRequest event) {
		this.users.get(session.user.userId).signal(session.sessionToken, event);
	}

	public void subscribeSession(final SyncServiceSession session, final SubscribeAreaRequest event) {
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

	public void unsubscribeSession(final SyncServiceSession session, final UnsubscribeAreaRequest event) {

		// unlink
		this.sessions.remove(session.sessionToken);
		this.users.computeIfPresent(session.user.userId, (id, user) -> user.remove(session) ? null : user);

		// response
		final UnsubscribeAreaResponse response = new UnsubscribeAreaResponse(event.id, this.areaId);
		this.service.protocol.send(session.sessionToken, response);
	}
}
