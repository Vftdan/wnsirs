package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetPheromoneImportance extends MethodDescriptor<Void, Double> {
	protected static GetPheromoneImportance instance = new GetPheromoneImportance();

	protected GetPheromoneImportance() {
		super("getPheromoneImportance", Void.class, Double.class);
	}

	public static GetPheromoneImportance getInstance() {
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
			return 1.0;
		}
	};
}
