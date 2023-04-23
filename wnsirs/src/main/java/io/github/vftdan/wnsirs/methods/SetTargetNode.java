package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetTargetNode extends SetSpecificValueMethodDescriptor<Node> {
	protected SetTargetNode(MethodImplementation<Node, Boolean> imp) {
		super("setTargetNode", Node.class, imp);
	}

	public static SetTargetNode getInstance() {
		return instance;
	}

	protected static SetTargetNode instance = new SetTargetNode(new AbstractImplementation<Node>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(GetAnt.getInstance());
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "targetNode";
		}
	});
}
