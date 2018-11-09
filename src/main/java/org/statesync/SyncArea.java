
package org.statesync;

import org.statesync.config.ClientAreaConfig;
import org.statesync.config.SyncAreaConfig;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.AreaSubscriptionError;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaResponse;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaResponse;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Supplier;

/**
 * Sync area. Process sync between server model and user sessions.
 *
 * @author ify
 *
 * @param <Model>
 */
public class SyncArea<Model>
{

	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(SyncArea.class.getName());
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
	final StateStorage storage;
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
	public SyncArea(final SyncAreaConfig<Model> config, final StateStorage storage, final StateReducer<Model> reducer,
			final SignalHandler<Model> signalHandler)
	{
		this.config = config;
		this.jsonFilter = new JsonFilter(config.getServerLocalPrefix(), config.getServerPush());
		this.storage = storage;
		this.reducer = reducer;
		this.signalHandler = signalHandler;
		this.areaId = config.getId();
		this.factory = () -> {
			try
			{
				return this.config.getModel().newInstance();
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e);
			}
		};
		this.synchronizer = new JsonSynchronizer<>(config.getModel());
		log.info("Area initialized:" + this.config);
	}

	protected boolean checkPermissions(final String userId)
	{
		return true;
	}

	public final String getAreaId()
	{
		return this.areaId;
	}

	protected ClientAreaConfig getClientConfig(final SyncAreaApi<Model> user)
	{
		return this.config.getClientConfig();
	}

	public SyncAreaConfig<Model> getConfig()
	{
		return this.config;
	}

	protected void onRegister(final SyncService service)
	{
		this.service = service;
	}

	public void patchArea(final SyncServiceSession session, final PatchAreaRequest event)
	{
		final String sessionToken = session.sessionToken;
		if (!checkPermissions(session.userId))
		{
			this.service.protocol.sendSubscribeAreaFail(sessionToken, event.id, this.areaId,
					AreaSubscriptionError.accessDenied);
			return;
		}
		// log.info("Trace: patch: " + session.sessionToken + ", " +
		// session.userId);
		final SyncAreaApi<Model> api = new SyncAreaApi<>(session, this);
		final ObjectNode initialJson = this.storage.load(session.sessionToken);
		final ObjectNode patchedJson = this.synchronizer.patch(initialJson, event.patch);
		final Model patchedModel = this.synchronizer.model(patchedJson);
		final Model reducedModel = this.reducer.reduce(patchedModel, api);
		final ObjectNode reducedJson = this.synchronizer.json(reducedModel);
		ArrayNode diff = this.synchronizer.diff(patchedJson, reducedJson);
		this.storage.save(session.sessionToken, reducedJson);
		// we must filter out server parts before sync.
		diff = this.jsonFilter.filterPatch(diff);
		if (diff.size() > 0)
		{
			this.service.protocol.sendPatch(sessionToken, this.areaId, diff);
		}
		this.service.protocol.confirmPatch(sessionToken, event);
	}

	public void signal(final SyncServiceSession session, final SignalRequest event)
	{
		final String sessionToken = session.sessionToken;
		if (!checkPermissions(session.userId))
		{
			this.service.protocol.sendSubscribeAreaFail(sessionToken, event.id, this.areaId,
					AreaSubscriptionError.accessDenied);
			return;
		}
		final SyncAreaApi<Model> api = new SyncAreaApi<>(session, this);
		final ObjectNode initialJson = this.storage.load(session.sessionToken);
		Model model = this.synchronizer.model(initialJson);
		model = this.reducer.reduce(model, api);
		model = this.signalHandler.handle(model, api, event.signal, event.parameters);
		model = this.reducer.reduce(model, api);
		final ObjectNode reducedJson = this.synchronizer.json(model);
		final ArrayNode diff = this.synchronizer.diff(initialJson, reducedJson);
		this.storage.save(session.sessionToken, reducedJson);
		this.service.protocol.sendPatch(sessionToken, this.areaId, diff);
		this.service.protocol.confirmSignal(sessionToken, event);
	}

	public void subscribeSession(final SyncServiceSession session, final SubscribeAreaRequest event)
	{
		synchronized (session)
		{
			final String sessionToken = session.sessionToken;
			// security
			if (!checkPermissions(session.userId))
			{
				this.service.protocol.sendSubscribeAreaFail(sessionToken, event.id, this.areaId,
						AreaSubscriptionError.accessDenied);
				return;
			}
			// link
			log.info("Trace: subscribe: " + session.sessionToken + ", " + session.userId);
			// load user model
			final ObjectNode json = this.storage.load(session.sessionToken);
			final Model model = json == null ? this.synchronizer.newModel() : this.synchronizer.model(json);
			final SyncAreaApi<Model> api = new SyncAreaApi<>(session, this);
			final Model updatedModel = this.reducer.reduce(model, api);
			final ObjectNode updatedJson = this.synchronizer.json(updatedModel);
			this.storage.save(session.sessionToken, updatedJson);
			this.service.protocol.send(sessionToken,
					new SubscribeAreaResponse(event.id, this.areaId, this.config.getClientConfig(), updatedJson));
		}
	}

	public void unregister()
	{
		this.service = null;
	}

	public void unsubscribeSession(final SyncServiceSession session, final UnsubscribeAreaRequest event)
	{
		final UnsubscribeAreaResponse response = new UnsubscribeAreaResponse(event.id, this.areaId);
		this.service.protocol.send(session.sessionToken, response);
	}

	public void fireChanges(final Dependency dependency, final SyncAreaApi<Model> syncAreaUser)
	{
		this.service.fireLocalChanges(dependency, syncAreaUser);
	}

	public void handleLocalChanges(final Dependency dependency, final SyncAreaApi<?> user)
	{
		// final SyncAreaApi<Model> u = this.users.get(user.getUserId());
		// if (u != null && u != user) {
		// if (u.hasDependency(dependency)) {
		// u.sync(null);
		// }
		// }
	}
}
