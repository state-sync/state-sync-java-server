package org.statesync.protocol;

public enum ResponseType {
	/**
	 *
	 */
	areaSubscriptionSuccess,
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
	areaUnsubscriptionSuccess,
	/**
	 * patchAreaSuccess. Short name is for traffic optimization
	 */
	s,
	/**
	 *
	 */
	patchAreaError;
}
