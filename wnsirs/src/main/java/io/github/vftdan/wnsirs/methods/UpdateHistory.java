package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.util.BareLinkedList;

public class UpdateHistory extends MethodDescriptor<Node, Void> {
	protected static UpdateHistory instance = new UpdateHistory();

	protected UpdateHistory() {
		super("updateHistory", Node.class, Void.class);
	}

	public static UpdateHistory getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Node, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Node, Void> defaultImplementation = new MethodImplementation<Node, Void>() {
		@Override
		public MethodDescriptor<? extends Node, ? super Void> implementationFor() {
			return instance;
		}

		@Override
		public Void call(AlgorithmPart root, Node args) {
			var history = root.callMethod(GetNodeHistory.getInstance(), null);
			root.callMethod(SetNodeHistory.getInstance(), new BareLinkedList<Node>(args, history));
			return null;
		}

		{
			dependencies = new Dependency[] {
				// Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(SetNodeHistory.getInstance()),
			};
		}
	};
}
