package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetAnt extends MethodDescriptor<Void, Ant> {
	protected static GetAnt instance = new GetAnt();

	protected GetAnt() {
		super("getAnt", Void.class, Ant.class);
	}

	public static GetAnt getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Ant> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Ant> defaultImplementation = new MethodImplementation<Void, Ant>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Ant> implementationFor() {
			return instance;
		}

		@Override
		public Ant call(AlgorithmPart root, Void args) {
			return null;
		}
	};
}
