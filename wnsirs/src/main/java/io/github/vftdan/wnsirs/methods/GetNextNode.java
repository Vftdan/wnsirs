package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetNextNode extends GetSpecificValueMethodDescriptor<Node> {
	protected GetNextNode(MethodImplementation<Void, Node> imp) {
		super("getNextNode", Node.class, imp);
	}

	public static GetNextNode getInstance() {
		return instance;
	}

	protected static GetNextNode instance = new GetNextNode(new AbstractImplementation<Node>() {
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
