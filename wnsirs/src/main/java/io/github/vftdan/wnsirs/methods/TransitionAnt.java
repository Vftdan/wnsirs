package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class TransitionAnt extends MethodDescriptor<Void, Void> {
	protected static TransitionAnt instance = new TransitionAnt();

	protected TransitionAnt() {
		super("transitionAnt", Void.class, Void.class);
	}

	public static TransitionAnt getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return null;
	}
}
