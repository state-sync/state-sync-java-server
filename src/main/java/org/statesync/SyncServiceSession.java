package org.statesync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.statesync.info.SyncSessionInfo;
import org.statesync.protocol.MessageQueue;
import org.statesync.protocol.RequestMessage;
import org.statesync.protocol.init.InitSessionResponse;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.signal.SignalRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

public class SyncServiceSession
{
	public final String sessionToken;
	public SyncService service;
	public String externalSessionId;
	public String userId;
	private final Map<String, MessageQueue> queues = new ConcurrentHashMap<>();
	private Executor executor;

	public SyncServiceSession(final SyncService service, final String userId, final String sessionToken,
			final String externalSessionId, final Executor executor)
	{
		this.service = service;
		this.userId = userId;
		this.sessionToken = sessionToken;
		this.externalSessionId = externalSessionId;
		this.executor = executor;
	}

	public SyncSessionInfo getInfo()
	{
		return new SyncSessionInfo(this.sessionToken, this.externalSessionId);
	}

	public void handle(final RequestMessage event)
	{
		// put event into queue
		final MessageQueue queue = this.queues.computeIfAbsent(event.area, (id) -> new MessageQueue(1));
		queue.put(event);
		// handle events in proper order, same thread handle events for same
		// session
		for (RequestMessage ready = queue.get(); ready != null; ready = queue.get())
		{
			handleInternal(ready);
		}
	}

	private void handleInternal(final RequestMessage ready)
	{
		this.executor.execute(this.sessionToken, () -> {
			switch (ready.getType())
			{
				case subscribeArea:
					subscribeArea((SubscribeAreaRequest) ready);
					return;
				case unsubscribeArea:
					unsubscribeArea((UnsubscribeAreaRequest) ready);
					return;
				case signal:
					signal((SignalRequest) ready);
					return;
				case p:
				default:
					patchArea((PatchAreaRequest) ready);
					return;
			}
		});
	}

	public InitSessionResponse init()
	{
		return new InitSessionResponse(this.sessionToken);
	}

	private void patchArea(final PatchAreaRequest event)
	{
		this.service.findArea(event.area).patchArea(this, event);
	}

	private void signal(final SignalRequest event)
	{
		this.service.findArea(event.area).signal(this, event);
	}

	private void subscribeArea(final SubscribeAreaRequest event)
	{
		this.service.findArea(event.area).subscribeSession(this, event);
	}

	private void unsubscribeArea(final UnsubscribeAreaRequest event)
	{
		this.service.findArea(event.area).unsubscribeSession(this, event);
	}

	public void logout()
	{
		this.service.logout(this.userId);
	}
}
