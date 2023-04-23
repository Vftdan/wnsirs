package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

public class GetEdgeLength extends MethodDescriptor<Void, Double> {
	protected static GetEdgeLength instance = new GetEdgeLength();

	protected GetEdgeLength() {
		super("getEdgeLength", Void.class, Double.class);
	}

	public static GetEdgeLength getInstance() {
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
			var edge = root.callMethod(GetEdge.getInstance());
			return root.callMethod(CalculateDistance.getInstance(), new Pair(edge.getStart().getPosition(), edge.getEnd().getPosition()));
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetEdge.getInstance()),
				Dependency.fromDescriptor(CalculateDistance.getInstance()),
			};
		}
	};
}
