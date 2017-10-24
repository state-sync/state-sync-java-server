package org.statesync.simple;

import org.junit.Assert;
import org.junit.Test;
import org.statesync.protocol.patch.PatchAreaRequest;
import org.statesync.protocol.subscription.SubscribeAreaRequest;
import org.statesync.protocol.subscription.UnsubscribeAreaRequest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JacksonUtils;

public class MainTest {

	private ArrayNode replacePatch(final String path, final String value) {
		final JsonNodeFactory f = JacksonUtils.nodeFactory();
		final ArrayNode arr = f.arrayNode();
		final ObjectNode obj = f.objectNode();
		obj.put("op", "replace");
		obj.put("path", path);
		obj.put("value", value);
		arr.add(obj);
		return arr;
	}

	@Test
	public void test() {
		int cid = 0;
		final ResponseCollector protocol = new ResponseCollector();
		final TestSyncService service = new TestSyncService(protocol);
		final TestArea area = new TestArea();
		service.register(area);
		final String sessionToken = service.connect("userA", "1", "2").sessionToken;
		service.handle(sessionToken, new SubscribeAreaRequest(cid++, "test"));

		Assert.assertEquals(1, service.getSessionsCount());
		Assert.assertEquals(1, service.getUserSubscriptionsCount());
		Assert.assertEquals(1, service.getSessionSubscriptionsCount());

		// check server patch
		service.handle(sessionToken, new PatchAreaRequest(cid, "test", replacePatch("/name", "a")));
		// Assert.assertEquals("a", TestArea.userStorage.byUser("userA").name);

		service.handle(sessionToken, new UnsubscribeAreaRequest(cid, "test"));

		Assert.assertEquals(1, service.getSessionsCount());
		Assert.assertEquals(0, service.getUserSubscriptionsCount());
		Assert.assertEquals(0, service.getSessionSubscriptionsCount());
		service.unregister("test");
	}
}
