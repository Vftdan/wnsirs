package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetEdge extends MethodDescriptor<Void, Edge> {
	protected static GetEdge instance = new GetEdge();

	protected GetEdge() {
		super("getEdge", Void.class, Edge.class);
	}

	public static GetEdge getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Edge> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Edge> defaultImplementation = new MethodImplementation<Void, Edge>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Edge> implementationFor() {
			return instance;
		}

		@Override
		public Edge call(AlgorithmPart root, Void args) {
			return null;
		}
	};
}
