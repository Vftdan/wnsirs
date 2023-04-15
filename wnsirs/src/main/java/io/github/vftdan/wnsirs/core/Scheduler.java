package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.locks.*;

import io.github.vftdan.wnsirs.util.*;

public class Scheduler {
	PriorityQueue<Task<?> > nextTasks;

	public static class Task<TArgs> {
		double startSimulationTime;
		Context context;
		MethodDescriptor<TArgs, ?> method;
		TArgs args;

		public Task(double startSimulationTime, Context context, MethodDescriptor<TArgs, ?> method, TArgs args) {
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
	}
}
