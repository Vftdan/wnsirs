package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetProbability extends MethodDescriptor<Void, Double> {
	protected static GetProbability instance = new GetProbability();

	protected GetProbability() {
		super("getProbability", Void.class, Double.class);
	}

	public static GetProbability getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Double> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Double> defaultImplementation = new MethodImplementation<Void, Double>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Double> implementationFor() {
			return instance;
		}

		@Override
		public Double call(AlgorithmPart root, Void args) {
			return Math.pow(root.callMethod(GetHeuristic.getInstance()), root.callMethod(GetHeuristicImportance.getInstance())) *
			       Math.pow(root.callMethod(GetPheromone.getInstance()), root.callMethod(GetPheromoneImportance.getInstance()));
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetHeuristic.getInstance()),
				Dependency.fromDescriptor(GetHeuristicImportance.getInstance()),
				Dependency.fromDescriptor(GetPheromone.getInstance()),
				Dependency.fromDescriptor(GetPheromoneImportance.getInstance()),
			};
		}
	};
}
