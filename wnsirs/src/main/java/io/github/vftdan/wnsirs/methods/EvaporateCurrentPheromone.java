package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class EvaporateCurrentPheromone extends MethodDescriptor<Void, Void> {
	private static EvaporateCurrentPheromone instance = new EvaporateCurrentPheromone();

	public static EvaporateCurrentPheromone getInstance() {
		return instance;
	}

	protected EvaporateCurrentPheromone() {
		super("evaporateCurrentPheromone", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return EvaporateCurrentPheromone.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			double pheromone = root.callMethod(GetPheromone.getInstance());
			pheromone *= root.callMethod(GetPheromoneRetention.getInstance());
			pheromone = Math.max(root.callMethod(GetMinPheromone.getInstance()), Math.min(pheromone, root.callMethod(GetMaxPheromone.getInstance())));
			root.callMethod(SetPheromone.getInstance(), pheromone);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetPheromone.getInstance()),
				Dependency.fromDescriptor(GetPheromoneRetention.getInstance()),
				Dependency.fromDescriptor(GetMinPheromone.getInstance()),
				Dependency.fromDescriptor(GetMaxPheromone.getInstance()),
				Dependency.fromDescriptor(SetPheromone.getInstance()),
			};
		}
	};
}
