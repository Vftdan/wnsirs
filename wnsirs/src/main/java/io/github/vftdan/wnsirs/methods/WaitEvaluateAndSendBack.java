package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class WaitEvaluateAndSendBack extends MethodDescriptor<Void, Void> {
	private static WaitEvaluateAndSendBack instance = new WaitEvaluateAndSendBack();

	public static WaitEvaluateAndSendBack getInstance() {
		return instance;
	}

	protected WaitEvaluateAndSendBack() {
		super("waitEvaluateAndSendBack", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return WaitEvaluateAndSendBack.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			root.callMethod(EvaluateAndSendBack.getInstance());
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(EvaluateAndSendBack.getInstance()),
			};
		}
	};
}
