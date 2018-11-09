
package org.statesync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Supplier;
import java.util.logging.Level;

public class Executor
{

	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Executor.class.getName());
	private Thread[] threads;
	private ArrayBlockingQueue<Runnable>[] queues;
	private int count;
	private Supplier<ThreadContextInheritance> contextInheritance;

	@SuppressWarnings("unchecked")
	public Executor(final Supplier<ThreadContextInheritance> contextInheritance, final int count)
	{
		this.contextInheritance = contextInheritance == null ? ThreadContextInheritance.DEFAULT : contextInheritance;
		this.count = count;
		this.threads = new Thread[count];
		this.queues = new ArrayBlockingQueue[count];
		for (int i = 0; i < count; i++)
		{
			this.queues[i] = new ArrayBlockingQueue<>(1000);
			final int j = i;
			final Thread thread = this.threads[i] = new Thread(() -> {
				while (true)
				{
					try
					{
						final Runnable task = this.queues[j].take();
						try
						{
							task.run();
						}
						catch (final Exception e)
						{
							log.log(Level.SEVERE, "HashExecutor", e);
						}
					}
					catch (final InterruptedException e1)
					{
						throw new RuntimeException(e1);
					}
				}
			}, "HashExecutor[" + i + "]");
			thread.setDaemon(true);
			thread.start();
		}
	}

	public void execute(final String key, final Runnable task)
	{
		final ThreadContextInheritance in = this.contextInheritance.get();
		final int idx = Math.abs(key.hashCode()) % this.count;
		try
		{
			in.grab();
			this.queues[idx].put(() -> {
				try
				{
					in.propagate();
					task.run();
				}
				finally
				{
					in.clear();
				}
			});
		}
		catch (final InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
