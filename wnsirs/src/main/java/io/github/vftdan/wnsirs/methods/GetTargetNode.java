package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetTargetNode extends GetSpecificValueMethodDescriptor<Node> {
	protected GetTargetNode(MethodImplementation<Void, Node> imp) {
		super("getTargetNode", Node.class, imp);
	}

	public static GetTargetNode getInstance() {
		return instance;
	}

	protected static GetTargetNode instance = new GetTargetNode(new AbstractImplementation<Node>() {
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
