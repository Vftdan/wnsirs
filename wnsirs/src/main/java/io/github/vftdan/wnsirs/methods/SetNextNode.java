package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetNextNode extends SetSpecificValueMethodDescriptor<Node> {
	protected SetNextNode(MethodImplementation<Node, Boolean> imp) {
		super("setNextNode", Node.class, imp);
	}

	public static SetNextNode getInstance() {
		return instance;
	}

	protected static SetNextNode instance = new SetNextNode(new AbstractImplementation<Node>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "nextNode";
		}
	});
}
