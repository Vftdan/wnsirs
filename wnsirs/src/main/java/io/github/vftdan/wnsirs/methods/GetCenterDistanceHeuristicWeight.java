package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetCenterDistanceHeuristicWeight extends MethodDescriptor<Void, Double> {
	protected static GetCenterDistanceHeuristicWeight instance = new GetCenterDistanceHeuristicWeight();

	protected GetCenterDistanceHeuristicWeight() {
		super("getCenterDistanceHeuristicWeight", Void.class, Double.class);
	}

	public static GetCenterDistanceHeuristicWeight getInstance() {
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
			return 0.1;
		}
	};
}
