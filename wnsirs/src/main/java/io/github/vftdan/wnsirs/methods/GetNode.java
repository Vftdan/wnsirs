package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetNode extends MethodDescriptor<Void, Node> {
	protected static GetNode instance = new GetNode();

	protected GetNode() {
		super("getNode", Void.class, Node.class);
	}

	public static GetNode getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Node> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Node> defaultImplementation = new MethodImplementation<Void, Node>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Node> implementationFor() {
			return instance;
		}

		@Override
		public Node call(AlgorithmPart root, Void args) {
			return null;
		}
	};
}
