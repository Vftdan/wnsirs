package io.github.vftdan.wnsirs.core;

import io.github.vftdan.wnsirs.methods.GetAnt;

public class Ant extends SimulationObject {
	{
		defaultRole = "ant";
		registerImplementation(GetAnt.getInstance(), new MethodImplementation<Void, Ant>() {
			@Override
			public MethodDescriptor<? extends Void, ? super Ant> implementationFor() {
				return GetAnt.getInstance();
			}

			@Override
			public Ant call(AlgorithmPart root, Void args) {
				return Ant.this;
			}
		});
	}
}
