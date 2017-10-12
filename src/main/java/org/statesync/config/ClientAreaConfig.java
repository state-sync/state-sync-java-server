package org.statesync.config;

import lombok.Data;

/**
 * Area config on client side
 *
 * @author ify
 *
 */
@Data
public class ClientAreaConfig {
	private String clientLocalPrefix = "$";
	private String[] clientPush = { "/" };
	private int timeout;
}
