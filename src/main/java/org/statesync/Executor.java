package org.statesync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;

import lombok.extern.java.Log;

@Log
public class Executor {

	private Thread[] threads;

	private ArrayBlockingQueue<Runnable>[] queues;

	private int count;

	@SuppressWarnings("unchecked")
	public Executor(final int count) {
		this.count = count;
		this.threads = new Thread[count];
		this.queues = new ArrayBlockingQueue[count];
		for (int i = 0; i < count; i++) {
			this.queues[i] = new ArrayBlockingQueue<>(1000);
			final int j = i;
			final Thread thread = this.threads[i] = new Thread(() -> {
				while (true) {
					try {
						final Runnable task = this.queues[j].take();
						try {
							task.run();
						} catch (final Exception e) {
							log.log(Level.SEVERE, "HashExecutor", e);
						}
					} catch (final InterruptedException e1) {
						throw new RuntimeException(e1);
					}
				}
			}, "HashExecutor[" + i + "]");

			thread.setDaemon(true);
			thread.start();
		}
	}

	public void execute(final String key, final Runnable task) {
		final int idx = Math.abs(key.hashCode()) % this.count;
		try {
			this.queues[idx].put(task);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
