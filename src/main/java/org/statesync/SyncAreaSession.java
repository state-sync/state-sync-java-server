package org.statesync;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.EventMessage;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaResponse;
import org.statesync.protocol.sync.PatchAreaEvent;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SyncAreaSession<Model> {

	private StateStorage sessionStorage;

	private SyncSession session;

	private JsonSynchronizer<Model> synchronizer;

	private String areaId;

	private SyncOutbound protocol;

	private SyncAreaUser<Model> user;

	public SyncAreaSession(final SyncSession session, final SyncAreaUser<Model> user) {
		super();
		this.session = session;
		this.user = user;
		this.areaId = user.area.getAreaId();
		this.protocol = user.area.service.protocol;
		this.synchronizer = user.area.synchronizer;
		this.sessionStorage = user.area.sessionStorage;
		user.sessions.put(session.sessionToken, this);
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	private ArrayNode filterOutputModel(final ArrayNode patch) {
		// TODO: Implement filtering
		return patch;
	}

	private ObjectNode filterOutputModel(final ObjectNode json) {
		// TODO: Implement filtering
		return json;
	}

	public void onChange(final Model updated) {
		final String sessionToken = this.session.sessionToken;
		final Model shadow = this.synchronizer.model(this.sessionStorage.load(this.session.sessionToken));
		ArrayNode patch = this.synchronizer.diff(shadow, updated);
		if (patch.size() > 0) {
			this.sessionStorage.save(sessionToken, this.synchronizer.json(updated));
		}
		patch = filterOutputModel(patch);
		if (patch.size() > 0) {
			final EventMessage event = new PatchAreaEvent(this.areaId, patch);
			// send changes sessions
			this.protocol.send(sessionToken, event);
		}
	}

	public void onRemove() {
		this.sessionStorage.remove(this.session.sessionToken);
	}

	public void subscribe(final SubscribeAreaRequest event) {
		final String sessionToken = this.session.sessionToken;
		// load user model
		final Model model = this.user.load();
		// Store model into session storage
		this.sessionStorage.save(sessionToken, this.synchronizer.json(model));
		final ObjectNode json = filterOutputModel(this.synchronizer.json(model));

		// response
		final ClientAreaConfig clientConfig = this.user.getClientConfig();
		final SubscribeAreaResponse response = new SubscribeAreaResponse(event.id, this.areaId, clientConfig, json);
		this.protocol.send(sessionToken, response);
	}
}
