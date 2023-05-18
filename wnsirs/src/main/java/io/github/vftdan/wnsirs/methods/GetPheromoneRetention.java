package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetPheromoneRetention extends MethodDescriptor<Void, Double> {
	protected static GetPheromoneRetention instance = new GetPheromoneRetention();

	protected GetPheromoneRetention() {
		super("getPheromoneRetention", Void.class, Double.class);
	}

	public static GetPheromoneRetention getInstance() {
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
			return 0.9;
		}
	};
}
