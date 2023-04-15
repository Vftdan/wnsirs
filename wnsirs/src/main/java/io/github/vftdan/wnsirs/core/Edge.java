package io.github.vftdan.wnsirs.core;

import io.github.vftdan.wnsirs.methods.GetEdge;

public class Edge extends SimulationObject {
	{
		defaultRole = "edge";
		registerImplementation(GetEdge.getInstance(), new MethodImplementation<Void, Edge>() {
			@Override
			public MethodDescriptor<? extends Void, ? super Edge> implementationFor() {
				return GetEdge.getInstance();
			}

			@Override
			public Edge call(AlgorithmPart root, Void args) {
				return Edge.this;
			}
		});
	}
}
