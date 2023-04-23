package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class HasArrived extends MethodDescriptor<Void, Boolean> {
	protected static HasArrived instance = new HasArrived();

	protected HasArrived() {
		super("hasArrived", Void.class, Boolean.class);
	}

	public static HasArrived getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Boolean> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Boolean> defaultImplementation = new MethodImplementation<Void, Boolean>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Boolean> implementationFor() {
			return instance;
		}

		@Override
		public Boolean call(AlgorithmPart root, Void args) {
			return root.callMethod(GetNode.getInstance()).equals(root.callMethod(GetTargetNode.getInstance()));
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNode.getInstance()),
				Dependency.fromDescriptor(GetTargetNode.getInstance()),
			};
		}
	};
}
