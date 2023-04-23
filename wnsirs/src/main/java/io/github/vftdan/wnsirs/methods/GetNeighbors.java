package io.github.vftdan.wnsirs.methods;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;

public class GetNeighbors extends MethodDescriptor<Void, Collection<Node> > {
	protected static GetNeighbors instance = new GetNeighbors();

	protected GetNeighbors() {
		super("getNeighbors", Void.class, (Class<Collection<Node> >)(Class<?>)Collection.class);
	}

	public static GetNeighbors getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Collection<Node> > getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Collection<Node> > defaultImplementation = new MethodImplementation<Void, Collection<Node> >() {
		@Override
		public MethodDescriptor<? extends Void, ? super Collection<Node> > implementationFor() {
			return instance;
		}

		@Override
		public Collection<Node> call(AlgorithmPart root, Void args) {
			var neighbors = new HashSet<Node>(root.callMethod(GetNode.getInstance(), null).neighbors);
			root.callMethod(FilterNeighbors.Emitter.getInstance(), neighbors);
			return neighbors;
		}
	};
}
