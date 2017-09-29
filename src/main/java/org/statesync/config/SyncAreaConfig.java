package org.statesync.config;

import lombok.Data;

@Data
public class SyncAreaConfig {
	private String channel = "/syncArea/";
	private String clientLocalPrefix = "$";
	private String[] clientPush = { "/" };
	private String id;
	private Class<?> model;
	private String serverLocalPrefix = "$";
	private String[] serverPush = { "/" };

	public SyncAreaConfig() {

	}

	public ClientAreaConfig getClientConfig() {
		final ClientAreaConfig cfg = new ClientAreaConfig();
		cfg.setClientLocalPrefix(this.clientLocalPrefix);
		cfg.setClientPush(this.clientPush);
		cfg.setId(this.id);
		return cfg;
	}
}
