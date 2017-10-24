package org.statesync;

import java.util.logging.Level;

import org.statesync.config.ClientAreaConfig;
import org.statesync.protocol.EventMessage;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaResponse;
import org.statesync.protocol.sync.PatchAreaEvent;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.java.Log;

@Log
public class SyncAreaSession<Model> {

	private StateStorage sessionStorage;

	private SyncServiceSession session;

	private JsonSynchronizer<Model> synchronizer;

	private String areaId;

	private SyncOutbound protocol;

	SyncAreaUser<Model> user;

	private JsonFilter jsonFilter;

	public SyncAreaSession(final SyncServiceSession session, final SyncAreaUser<Model> user) {
		super();
		this.session = session;
		this.user = user;
		this.jsonFilter = user.area.jsonFilter;
		this.areaId = user.area.getAreaId();
		this.protocol = user.area.service.protocol;
		this.synchronizer = user.area.synchronizer;
		this.sessionStorage = user.area.sessionStorage;
		user.sessions.put(session.sessionToken, this);
	}

	public void accept(final SyncServiceVisitor visitor) {
		visitor.visit(this);
	}

	public void onChange(final Model updated) {
		try {
			final String sessionToken = this.session.sessionToken;
			final ObjectNode json = this.sessionStorage.load(this.session.sessionToken);
			final Model shadow = this.synchronizer.model(json);
			ArrayNode patch = this.synchronizer.diff(shadow, updated);
			// we must filter out server parts before sync.
			patch = this.jsonFilter.filterPatch(patch);
			if (patch.size() > 0) {
				final Model updatedSession = this.synchronizer.patch(shadow, patch);
				this.sessionStorage.save(sessionToken, this.synchronizer.json(updatedSession));
				final EventMessage event = new PatchAreaEvent(this.areaId, patch);
				// send changes sessions
				this.protocol.send(sessionToken, event);
			}
		} catch (final Exception e) {
			log.log(Level.SEVERE, "Session was not completed correctly", e);
		}
	}

	public void onRemove() {
		this.sessionStorage.remove(this.session.sessionToken);
	}

	public void patch(final PatchAreaRequest event) {
		final ObjectNode json = this.sessionStorage.load(this.session.sessionToken);
		final Model model = this.synchronizer.patch(this.synchronizer.model(json), event.patch);
		this.sessionStorage.save(this.session.sessionToken, this.synchronizer.json(model));
	}

	public void subscribe(final SubscribeAreaRequest event) {
		final String sessionToken = this.session.sessionToken;
		// load user model
		final Model model = this.user.load();
		// Store model into session storage
		ObjectNode json = this.synchronizer.json(model);
		this.sessionStorage.save(sessionToken, json);
		json = this.jsonFilter.filterModel(json);

		// response
		final ClientAreaConfig clientConfig = this.user.getClientConfig();
		final SubscribeAreaResponse response = new SubscribeAreaResponse(event.id, this.areaId, clientConfig, json);
		this.protocol.send(sessionToken, response);
	}
}
