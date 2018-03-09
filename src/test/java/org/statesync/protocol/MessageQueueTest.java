package org.statesync.protocol;

import org.junit.Assert;
import org.junit.Test;

public class MessageQueueTest {

	@Test
	public void test() {
		final MessageQueue q = new MessageQueue();
		Assert.assertNull(q.get());
		q.put(message(0));
		Assert.assertNotNull(q.get());
		q.put(message(2));
		Assert.assertNull(q.get());
		q.put(message(1));
		Assert.assertNotNull(q.get());
		Assert.assertNotNull(q.get());
		Assert.assertNull(q.get());
	}

	private RequestMessage message(final int i) {
		return new RequestMessage(i, RequestType.p, "a") {
		};
	}

}
