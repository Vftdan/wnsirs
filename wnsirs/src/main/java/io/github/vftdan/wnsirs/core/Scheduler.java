package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import io.github.vftdan.wnsirs.util.*;

public class Scheduler {
	PriorityQueue<Task<?> > nextTasks;
	Set<Task<?> > runningTasks = new HashSet();
	double simulationTime = 0.0;
	double minimalDelay = 0.01;
	Lock schedulerLock = new ReentrantLock();
	Condition timeAdvanced = schedulerLock.newCondition();
	Condition lockAcquired = schedulerLock.newCondition();
	ExecutorService executor;

	{
		var numCores = Runtime.getRuntime().availableProcessors();
		numCores = 1;
		executor = new ThreadPoolExecutor(numCores, numCores, 0, TimeUnit.SECONDS, new ArrayBlockingQueue(8));

		nextTasks = new PriorityQueue<Task<?> >((lhs, rhs) -> ((Double) lhs.startSimulationTime).compareTo(rhs.startSimulationTime));
	}

	public <TArgs> void scheduleTask(double delay, Context context, MethodDescriptor<TArgs, ?> method, TArgs args) {
		var time = context.getSimulationTime() + Math.max(delay, minimalDelay);
		var task = new Task<TArgs>(this, time, context, method, args);
		schedulerLock.lock();
		try {
			nextTasks.add(task);
			timeAdvanced.signalAll();
		} finally {
			schedulerLock.unlock();
		}
	}

	public static class Task<TArgs> implements Runnable {
		Scheduler scheduler;
		double startSimulationTime;
		Context context;
		MethodDescriptor<TArgs, ?> method;
		TArgs args;
		volatile boolean lockAcquired = false;
		volatile boolean finished = false;

		public Task(Scheduler scheduler, double startSimulationTime, Context context, MethodDescriptor<TArgs, ?> method, TArgs args) {
			this.scheduler = scheduler;
			this.startSimulationTime = startSimulationTime;
			this.context = context;
			this.method = method;
			this.args = args;
		}

		public Lock getLock() {
			ArrayList<Lock> list = new ArrayList<Lock>();
			var deps = MethodImplementation.getCachedInclusiveTransitiveDependencies(this.context, this.context.getPart("algorithm"), this.method, this.args);
			for (var dep: deps) {
				list.addAll(dep.getImplementationLocks(this.context));
			}
			return new CompositeLock(list);
		}

		@Override
		public void run() {
			try {
				var lock = getLock();
				lock.lock();
				try {
					lockAcquired = true;
					scheduler.schedulerLock.lock();
					try {
						scheduler.lockAcquired.signalAll();
					} finally {
						scheduler.schedulerLock.unlock();
					}
					context.advanceSimulationTime(startSimulationTime - context.getSimulationTime());
					context.callMethod(method, args);
				} finally {
					lock.unlock();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				finished = true;
			}
			this.scheduler.taskFinished(this);
		}
	}

	public void schedulingLoop() throws InterruptedException {
		boolean running = true;
mainLoop:
		while (running) {
			schedulerLock.lock();
			try {
				while (nextTasks.isEmpty()) {
					if (runningTasks.isEmpty()) {
						running = false;
						break mainLoop;
					}
					timeAdvanced.await();
				}
				while (nextTasks.peek().startSimulationTime - minimalDelay > simulationTime) {
					timeAdvanced.await();
				}
				var task = nextTasks.poll();
				executor.execute(task);
				runningTasks.add(task);
				while (!task.lockAcquired && !task.finished) {
					lockAcquired.await();
				}
			} finally {
				schedulerLock.unlock();
			}
		}
	}

	public void terminate() {
		this.executor.shutdown();
	}

	public void taskFinished(Task<?> task) {
		schedulerLock.lock();
		try {
			runningTasks.remove(task);
			// update simulationTime
			double time = Double.POSITIVE_INFINITY;
			for (var otherTask: runningTasks) {
				time = Math.min(time, otherTask.startSimulationTime);
			}
			if (!nextTasks.isEmpty())
				time = Math.min(time, nextTasks.peek().startSimulationTime);
			simulationTime = time;
			timeAdvanced.signalAll();
		} finally {
			schedulerLock.unlock();
		}
	}
}
