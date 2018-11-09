package org.statesync;

import java.util.function.Supplier;

/**
 * Provide thread context inheritance, used to propagate spring security context
 * into thread pool
 *
 * @author ify
 *
 */
public interface ThreadContextInheritance
{
	public static final Supplier<ThreadContextInheritance> DEFAULT = () -> new ThreadContextInheritance()
	{

		@Override
		public void grab()
		{
		}

		@Override
		public void propagate()
		{
		}

		@Override
		public void clear()
		{
		}

	};

	/**
	 * Grab context from source thread
	 */
	public void grab();

	/**
	 * Propagate context into child thread in thread pool.
	 */
	public void propagate();

	/**
	 * Clear context in child thread
	 */
	public void clear();
}
