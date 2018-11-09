package org.statesync;

public class SyncException extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1251226217969557946L;

	public SyncException(final String message)
	{
		super(message);
	}

	public SyncException(final Exception e)
	{
		super(e);
	}

}
