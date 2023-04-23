package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetTransmissionDelay extends MethodDescriptor<Void, Double> {
	private static GetTransmissionDelay instance = new GetTransmissionDelay();

	public static GetTransmissionDelay getInstance() {
		return instance;
	}

	protected GetTransmissionDelay() {
		super("getTransmissionDelay", Void.class, Double.class);
	}

	@Override
	public MethodImplementation<Void, Double> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Double> defaultImplementation = new MethodImplementation<Void, Double>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Double> implementationFor() {
			return GetTransmissionDelay.getInstance();
		}

		@Override
		public Double call(AlgorithmPart root, Void args) {
			return .0;
		}
	};
}
