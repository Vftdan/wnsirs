package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.util.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetNodeHistory extends GetSpecificValueMethodDescriptor<BareLinkedList<Node> > {
	protected GetNodeHistory(MethodImplementation<Void, BareLinkedList<Node> > imp) {
		super("getNodeHistory", (Class<BareLinkedList<Node> >) (Class<?>) BareLinkedList.class, imp);
	}

	public static GetNodeHistory getInstance() {
		return instance;
	}

	protected static GetNodeHistory instance = new GetNodeHistory(new AbstractImplementation<BareLinkedList<Node> >() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "nodeHistory";
		}
	});
}
