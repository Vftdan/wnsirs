package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class AntArrived extends MethodDescriptor<Void, Void> {
	private static AntArrived instance = new AntArrived();

	public static AntArrived getInstance() {
		return instance;
	}

	protected AntArrived() {
		super("antArrived", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return AntArrived.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			root.callMethod(DestroyAnt.Emitter.getInstance());
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(DestroyAnt.Emitter.getInstance()),
			};
		}
	};
}
