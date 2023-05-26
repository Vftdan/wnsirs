package io.github.vftdan.wnsirs.core;

import java.util.*;

public class Context extends CompositeAlgorithmPart implements Cloneable {
	protected Random random = new Random();
	protected double simulationTime = 0;
	protected Scheduler scheduler;
	protected long seed = 0;
	protected long numClones = 0;

	protected Context() {
		defaultRole = "context";
	}

	public Context(Scheduler scheduler, long seq) {
		this();
		seed = seq;
		this.scheduler = scheduler;
		this.init();
	}

	public Context(Scheduler scheduler) {
		this(scheduler, 0);
	}

	public Context clone() {
		var other = new Context();
		other.defaultRole = defaultRole;
		for (var key: possibleImplementationChildren.keySet())
			other.possibleImplementationChildren.put(key, new LinkedList<String>(possibleImplementationChildren.get(key)));
		other.parts.putAll(parts);
		other.simulationTime = simulationTime;
		other.scheduler = scheduler;
		other.seed = seed + (++numClones);
		other.init();
		return other;
	}

	public void resetClones() {
		numClones = 0;
	}

	public void init() {
		Double time = simulationTime;
		seed ^= time.hashCode();
		random.setSeed(seed);
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public double getSimulationTime() {
		return simulationTime;
	}

	public void advanceSimulationTime(double amount) {
		simulationTime += amount;
	}

	public Random getRandom() {
		return random;
	}

	public static class GetScheduler extends MethodDescriptor<Void, Scheduler> {
		private static GetScheduler instance = new GetScheduler();

		public static GetScheduler getInstance() {
			return instance;
		}

		protected GetScheduler() {
			super("getScheduler", Void.class, Scheduler.class);
		}

		@Override
		public MethodImplementation<Void, Scheduler> getDefaultImplementation() {
			return defaultImplementation;
		}

		private MethodImplementation<Void, Scheduler> defaultImplementation = new MethodImplementation<Void, Scheduler>() {
			@Override
			public MethodDescriptor<? extends Void, ? super Scheduler> implementationFor() {
				return GetScheduler.getInstance();
			}

			@Override
			public Scheduler call(AlgorithmPart root, Void args) {
				if (!(root instanceof Context))
					return null;
				return ((Context) root).getScheduler();
			}
		};
	}
}
