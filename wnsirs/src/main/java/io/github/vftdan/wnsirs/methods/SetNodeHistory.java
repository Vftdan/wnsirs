package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.util.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetNodeHistory extends SetSpecificValueMethodDescriptor<BareLinkedList<Node> > {
	protected SetNodeHistory(MethodImplementation<BareLinkedList<Node> , Boolean> imp) {
		super("getNodeHistory", (Class<BareLinkedList<Node> >) (Class<?>) BareLinkedList.class, imp);
	}

	public static SetNodeHistory getInstance() {
		return instance;
	}

	protected static SetNodeHistory instance = new SetNodeHistory(new AbstractImplementation<BareLinkedList<Node> >() {
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
