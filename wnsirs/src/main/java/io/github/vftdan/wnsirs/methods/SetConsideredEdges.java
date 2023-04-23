package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

import java.util.Collection;

public class SetConsideredEdges extends SetSpecificValueMethodDescriptor<Collection<Edge> > {
	protected SetConsideredEdges(MethodImplementation<Collection<Edge> , Boolean> imp) {
		super("setConsideredEdges", (Class<Collection<Edge> >)(Class<?>)Collection.class, imp);
	}

	public static SetConsideredEdges getInstance() {
		return instance;
	}

	protected static SetConsideredEdges instance = new SetConsideredEdges(new AbstractImplementation<Collection<Edge> >() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetEdge.getInstance())};
			valueName = "consideredEdges";
		}
	});
}
