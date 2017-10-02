package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.statesync.config.ClientAreaConfig;
import org.statesync.config.SyncAreaConfig;
import org.statesync.protocol.EventMessage;
import org.statesync.protocol.patch.PatchAreaFail;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.patch.PatchAreaResponse;
import org.statesync.protocol.patch.PatchError;
import org.statesync.protocol.subscription.AreaSubscriptionError;
import org.statesync.protocol.subscription.SubscribeAreaFail;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaResponse;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaResponse;
import org.statesync.protocol.sync.PatchAreaEvent;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.java.Log;

@Log
public class SyncArea<Model> {
	private final Map<String, SyncAreaUser> users = new ConcurrentHashMap<>();
	private final Map<String, SyncSession> sessions = new ConcurrentHashMap<>();
	private SyncService service;
	private final String areaId;
	protected final JsonSynchronizer<Model> synchronizer;
	private SyncAreaConfig<Model> config;
	private StateStorage<Model> storage;
	private StateProcessor<Model> processor;

	public SyncArea(final SyncAreaConfig<Model> config, final StateStorage<Model> storage,
			final StateProcessor<Model> processor) {
		this.config = config;
		this.storage = storage;
		this.processor = processor;
		this.areaId = config.getId();
		this.synchronizer = new JsonSynchronizer<>(config.getModel());
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	protected boolean checkPermissions(final String userId) {
		return true;
	}

	private ArrayNode filterOutputModel(final ArrayNode patch) {
		// TODO Auto-generated method stub
		return patch;
	}

	private ObjectNode filterOutputModel(final ObjectNode json) {
		// TODO: server push filter
		// this.config.getServerPush()
		return json;
	}

	public final String getAreaId() {
		return this.areaId;
	}

	protected ClientAreaConfig getClientConfig(final SyncAreaUser user) {
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

	public void patchArea(final SyncSession session, final PatchAreaRequest request) {
		final SyncAreaUser user = this.users.get(session.user.userId);
		if (user == null) {
			this.service.disconnectUser(session.user.userId);
			return;
		}
		try {
			// patch model
			final Model original = this.storage.load(user);
			Model updated = this.synchronizer.patch(original, request.patch);
			updated = this.processor.process(updated, user);
			this.storage.save(updated, user);
			ArrayNode patch = this.synchronizer.diff(original, updated);
			patch = filterOutputModel(patch);
			if (patch.size() > 0) {
				final EventMessage event = new PatchAreaEvent(this.areaId, patch);
				// broadcast to all user sessions
				this.service.protocol.broadcast(session.user.userToken, event);
			}
			// send confirmation to original session
			this.service.protocol.send(session.sessionToken, new PatchAreaResponse(request.id, this.areaId));
		} catch (final Exception e) {
			log.log(Level.SEVERE, "patch error", e);
			this.service.protocol.send(session.sessionToken,
					new PatchAreaFail(request.id, this.areaId, PatchError.unknown));
		}
	}

	public void subscribeSession(final SyncSession session, final SubscribeAreaRequest event) {

		final String sessionToken = session.sessionToken;

		// security
		if (!checkPermissions(session.user.userId)) {
			this.service.protocol.send(sessionToken,
					new SubscribeAreaFail(event.id, this.areaId, AreaSubscriptionError.accessDenied));
			return;
		}

		// link
		final SyncAreaUser user = this.users.computeIfAbsent(session.user.userId,
				id -> new SyncAreaUser(session.user.userId, this.service.newUserToken(session.user.userId)));
		user.sessions.put(sessionToken, session);

		// sync model
		Model model = this.storage.load(user);
		model = this.processor.process(model, user);
		// TODO: notify other user sessions about changes
		this.storage.save(model, user);
		final ObjectNode json = filterOutputModel(this.synchronizer.json(model));

		// response
		final ClientAreaConfig clientConfig = getClientConfig(user);
		final SubscribeAreaResponse response = new SubscribeAreaResponse(event.id, this.areaId, clientConfig, json);
		this.service.protocol.send(sessionToken, response);
	}

	public void sync(final SyncAreaUser user, final Synchronizer<Model> synchronizer) {
		final Model original = this.storage.load(user);
		Model updated = this.synchronizer.clone(original);
		updated = this.processor.process(updated, user);
		ArrayNode patch = this.synchronizer.diff(original, updated);
		if (patch.size() > 0) {
			this.storage.save(updated, user);
		}
		patch = filterOutputModel(patch);
		if (patch.size() > 0) {
			final EventMessage event = new PatchAreaEvent(this.areaId, patch);
			// broadcast to all user sessions
			this.service.protocol.broadcast(user.userToken, event);
		}
	}

	public void sync(final Synchronizer<Model> synchronizer) {
		this.users.forEach((k, user) -> {
			sync(user, synchronizer);
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
