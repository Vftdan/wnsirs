package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetMaxPheromone extends MethodDescriptor<Void, Double> {
	protected static GetMaxPheromone instance = new GetMaxPheromone();

	protected GetMaxPheromone() {
		super("getMaxPheromone", Void.class, Double.class);
	}

	public static GetMaxPheromone getInstance() {
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
			return Double.MAX_VALUE;
		}
	};
}
