package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetHeuristic extends MethodDescriptor<Void, Double> {
	protected static GetHeuristic instance = new GetHeuristic();

	protected GetHeuristic() {
		super("getHeuristic", Void.class, Double.class);
	}

	public static GetHeuristic getInstance() {
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
			return 1.0 / (root.callMethod(GetEdgeLength.getInstance()) + 0.0001);
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetEdgeLength.getInstance()),
			};
		}
	};
}
