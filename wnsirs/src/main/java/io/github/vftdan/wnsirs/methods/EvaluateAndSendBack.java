package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class EvaluateAndSendBack extends MethodDescriptor<Void, Void> {
	private static EvaluateAndSendBack instance = new EvaluateAndSendBack();

	public static EvaluateAndSendBack getInstance() {
		return instance;
	}

	protected EvaluateAndSendBack() {
		super("evaluateAndSendBack", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return EvaluateAndSendBack.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			root.callMethod(SetTotalNodeHistory.getInstance(), root.callMethod(GetNodeHistory.getInstance()));
			root.callMethod(EvaluateAnt.getInstance());
			root.callMethod(SetAntBackward.getInstance(), true);
			root.callMethod(EvaporatePheromones.getInstance(), null);
			root.callMethod(TransitionAnt.getInstance());
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(SetTotalNodeHistory.getInstance()),
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(EvaluateAnt.getInstance()),
				Dependency.fromDescriptor(SetAntBackward.getInstance()),
				Dependency.fromDescriptor(EvaporatePheromones.getInstance()),
				Dependency.fromDescriptor(TransitionAnt.getInstance()),
			};
		}
	};
}
