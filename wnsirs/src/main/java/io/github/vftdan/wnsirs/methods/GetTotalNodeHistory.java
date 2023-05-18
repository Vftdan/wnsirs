package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.util.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetTotalNodeHistory extends GetSpecificValueMethodDescriptor<BareLinkedList<Node> > {
	protected GetTotalNodeHistory(MethodImplementation<Void, BareLinkedList<Node> > imp) {
		super("getTotalNodeHistory", (Class<BareLinkedList<Node> >) (Class<?>) BareLinkedList.class, imp);
	}

	public static GetTotalNodeHistory getInstance() {
		return instance;
	}

	protected static GetTotalNodeHistory instance = new GetTotalNodeHistory(new AbstractImplementation<BareLinkedList<Node> >() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		@Override
		protected BareLinkedList<Node> getFallbackValue(AlgorithmPart root) {
			return root.callMethod(GetNodeHistory.getInstance());
		}

		{
			dependencies = new Dependency<?>[] {
				Dependency.fromDescriptor(GetAnt.getInstance()),
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
			};
			valueName = "totalNodeHistory";
		}
	});
}
