package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class GetPreviousNode extends MethodDescriptor<Void, Node> {
	private static GetPreviousNode instance = new GetPreviousNode();

	public static GetPreviousNode getInstance() {
		return instance;
	}

	protected GetPreviousNode() {
		super("getPreviousNode", Void.class, Node.class);
	}

	@Override
	public MethodImplementation<Void, Node> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Node> defaultImplementation = new MethodImplementation<Void, Node>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Node> implementationFor() {
			return GetPreviousNode.getInstance();
		}

		@Override
		public Node call(AlgorithmPart root, Void args) {
			var history = root.callMethod(GetNodeHistory.getInstance(), null);
			if (history == null)
				return null;
			return history.value;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
			};
		}
	};
}
