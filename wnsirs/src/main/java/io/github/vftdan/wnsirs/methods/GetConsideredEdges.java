package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

import java.util.Collection;

public class GetConsideredEdges extends GetSpecificValueMethodDescriptor<Collection<Edge> > {
	protected GetConsideredEdges(MethodImplementation<Void, Collection<Edge> > imp) {
		super("getConsideredEdges", (Class<Collection<Edge> >)(Class<?>)Collection.class, imp);
	}

	public static GetConsideredEdges getInstance() {
		return instance;
	}

	protected static GetConsideredEdges instance = new GetConsideredEdges(new AbstractImplementation<Collection<Edge> >() {
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
