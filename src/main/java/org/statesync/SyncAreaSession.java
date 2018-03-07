package org.statesync;

import java.util.logging.Level;

import org.statesync.protocol.EventMessage;
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

	SyncAreaApi<Model> user;

	private JsonFilter jsonFilter;

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
}
