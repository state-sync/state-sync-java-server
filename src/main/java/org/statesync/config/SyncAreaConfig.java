package org.statesync.config;

import lombok.Data;

@Data
public class SyncAreaConfig<Model> {
	private String channel = "/syncArea/";
	private String clientLocalPrefix = "$";
	private String[] clientPush = { "/" };
	private int timeout = 60_000;
	private String id;
	private Class<Model> model;
	private String serverLocalPrefix = "$";
	private String[] serverPush = { "/" };

	public SyncAreaConfig() {

	}

	public ClientAreaConfig getClientConfig() {
		final ClientAreaConfig cfg = new ClientAreaConfig();
		cfg.setClientLocalPrefix(this.clientLocalPrefix);
		cfg.setClientPush(this.clientPush);
		cfg.setTimeout(this.timeout);
		return cfg;
	}
}
