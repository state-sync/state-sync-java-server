
package org.statesync.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageQueue
{
	private final List<RequestMessage> messages = new ArrayList<>();
	private int waitForId = 0;

	public RequestMessage get()
	{
		while (true)
		{
			synchronized (this)
			{
				if (!this.messages.isEmpty())
				{
					final RequestMessage msg = this.messages.get(0);
					if (msg.id == this.waitForId)
					{
						this.messages.remove(0);
						this.waitForId++;
						return msg;
					}
				}
				return null;
			}
		}
	}

	public void put(final RequestMessage msg)
	{
		synchronized (this)
		{
			this.messages.add(msg);
			Collections.sort(this.messages, (a, b) -> Integer.compare(a.id, b.id));
			this.notifyAll();
		}
	}

	public MessageQueue()
	{
	}

	public MessageQueue(final int waitForId)
	{
		this.waitForId = waitForId;
	}
}
