package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetNetwork extends MethodDescriptor<Void, Network> {
	protected static GetNetwork instance = new GetNetwork();

	protected GetNetwork() {
		super("getNetwork", Void.class, Network.class);
	}

	public static GetNetwork getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Network> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Network> defaultImplementation = new MethodImplementation<Void, Network>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Network> implementationFor() {
			return instance;
		}

		@Override
		public Network call(AlgorithmPart root, Void args) {
			return null;
		}
	};
}
