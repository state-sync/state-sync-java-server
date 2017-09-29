package org.statesync.protocol;

public enum RequestType {
	/**
	 * Subscribe user session to specific area
	 */
	subscribeArea,
	/**
	 * Subscribe user session to specific area
	 */
	unsubscribeArea,
	/**
	 * Client requested area patch
	 */
	patchArea
}
