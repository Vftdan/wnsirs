package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetTargetDistanceHeuristicWeight extends MethodDescriptor<Void, Double> {
	protected static GetTargetDistanceHeuristicWeight instance = new GetTargetDistanceHeuristicWeight();

	protected GetTargetDistanceHeuristicWeight() {
		super("getTargetDistanceHeuristicWeight", Void.class, Double.class);
	}

	public static GetTargetDistanceHeuristicWeight getInstance() {
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
			return 0.15;
		}
	};
}
