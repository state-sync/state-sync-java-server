package org.statesync.protocol;

public enum ResponseType {
	/**
	 *
	 */
	areaSubscriptionError,
	/**
	 *
	 */
	areaSubscription,
	/**
	 *
	 */
	areaUnsubscription,
	/**
	 * patchAreaSuccess. Short name is for traffic optimization
	 */
	s,
	/**
	 *
	 */
	patchAreaError, signalError, signalResponse;
}
