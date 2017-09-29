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

import lombok.Getter;
import lombok.extern.java.Log;

@Getter
@Log
public abstract class SyncArea<Model> {
	private final Map<String, SyncAreaUser> users = new ConcurrentHashMap<>();
	private final Map<String, SyncSession> sessions = new ConcurrentHashMap<>();
	private SyncService service;
	private final String id;
	private final JsonSynchronizer<Model> synchronizer;
	private SyncAreaConfig config;

	public SyncArea(final SyncAreaConfig config) {
		this.id = config.getId();
		this.config = config;
		this.synchronizer = new JsonSynchronizer<>(config);
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

	protected abstract ClientAreaConfig getConfig(SyncAreaUser user);

	public int getSessionsCount() {
		return this.sessions.size();
	}

	public int getUsersCount() {
		return this.users.size();
	}

	protected abstract Model loadModel(SyncAreaUser session);

	protected void onRegister(final SyncService service) {
		this.service = service;
	}

	public void patchArea(final SyncSession session, final PatchAreaRequest request) {
		final SyncAreaUser user = this.users.get(session.userId);
		if (user == null) {
			this.service.disconnectUser(session.userId);
			return;
		}
		try {
			// patch model
			final Model original = loadModel(user);
			Model updated = this.synchronizer.patch(original, request.patch);
			updated = process(updated, user);
			storeModel(updated, user);
			ArrayNode patch = this.synchronizer.diff(original, updated);
			patch = filterOutputModel(patch);

			final EventMessage event = new PatchAreaEvent(this.id, patch);
			// broadcast to all user sessions
			this.service.protocol.broadcast(session.userId, event);
			// send confirmation to original session
			this.service.protocol.send(session.sessionId, new PatchAreaResponse(request.id, getId()));
		} catch (final Exception e) {
			log.log(Level.SEVERE, "patch error", e);
			this.service.protocol.send(session.sessionId, new PatchAreaFail(request.id, getId(), PatchError.unknown));
		}

	}

	protected abstract Model process(Model model, SyncAreaUser user);

	protected abstract void storeModel(Model model, SyncAreaUser user);

	public void subscribeSession(final SyncSession session, final SubscribeAreaRequest event) {

		final String sessionId = session.sessionId;

		// security
		if (!checkPermissions(session.userId)) {
			this.service.protocol.send(sessionId,
					new SubscribeAreaFail(event.id, getId(), AreaSubscriptionError.accessDenied));
			return;
		}

		// link
		final SyncAreaUser user = this.users.computeIfAbsent(session.userId, id -> new SyncAreaUser(session.userId));
		this.sessions.put(sessionId, session);
		user.sessions.put(sessionId, session);

		// sync model
		Model model = loadModel(user);
		model = process(model, user);
		// TODO: notify other user sessions about changes
		storeModel(model, user);
		final ObjectNode json = filterOutputModel(this.synchronizer.json(model));

		// response
		final ClientAreaConfig clientConfig = getConfig(user);
		final SubscribeAreaResponse response = new SubscribeAreaResponse(event.id, getId(), clientConfig, json);
		this.service.protocol.send(sessionId, response);
	}

	public void sync(final Synchronizer<Model> synchronizer) {
		// this.sessions.forEach(action);
		// final Model original = loadModel(session);
		// final Model updated = process(original, session);
		// storeModel(updated, session);
		//
		// final JsonNode serverPatch = this.synchronizer.diff(original,
		// updated);
		// this.service.protocol.send(sessionId, event);
		// ServerPatch(this.principal, serverPatch);
		// storeModel(original, session);
	}

	public void unregister() {
		this.service = null;
	}

	public void unsubscribeSession(final SyncSession session, final UnsubscribeAreaRequest event) {

		// unlink
		this.sessions.remove(session.sessionId);
		this.users.computeIfPresent(session.userId, (id, user) -> user.remove(session) ? null : user);

		// response
		final UnsubscribeAreaResponse response = new UnsubscribeAreaResponse(event.id, getId());
		this.service.protocol.send(session.sessionId, response);
	}
}
