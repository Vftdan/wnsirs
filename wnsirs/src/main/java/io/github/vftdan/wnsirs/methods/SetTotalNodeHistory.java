package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.util.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetTotalNodeHistory extends SetSpecificValueMethodDescriptor<BareLinkedList<Node> > {
	protected SetTotalNodeHistory(MethodImplementation<BareLinkedList<Node> , Boolean> imp) {
		super("getTotalNodeHistory", (Class<BareLinkedList<Node> >) (Class<?>) BareLinkedList.class, imp);
	}

	public static SetTotalNodeHistory getInstance() {
		return instance;
	}

	protected static SetTotalNodeHistory instance = new SetTotalNodeHistory(new AbstractImplementation<BareLinkedList<Node> >() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "totalNodeHistory";
		}
	});
}
