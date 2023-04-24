package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

public class GetEdgeBetween extends MethodDescriptor<Pair<Node, Node>, Edge> {
	protected static GetEdgeBetween instance = new GetEdgeBetween();

	protected GetEdgeBetween() {
		super("getEdgeBetween", (Class<Pair<Node, Node> >)(Class<?>)Pair.class, Edge.class);
	}

	public static GetEdgeBetween getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Pair<Node, Node>, Edge> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Pair<Node, Node>, Edge> defaultImplementation = new MethodImplementation<Pair<Node, Node>, Edge>() {
		@Override
		public MethodDescriptor<? extends Pair<Node, Node>, ? super Edge> implementationFor() {
			return instance;
		}

		protected Edge createEdge(AlgorithmPart root, Pair<Node, Node> ends) {
			var edge = new Edge(ends.getKey(), ends.getValue());
			edge.callMethod(SetPheromone.getInstance(), root.callMethod(GetDefaultPheromone.getInstance()));
			root.callMethod(EdgeCreated.Emitter.getInstance(), edge);
			return edge;
		}

		@Override
		public Edge call(AlgorithmPart root, Pair<Node, Node> args) {
			var net = root.callMethod(GetNetwork.getInstance());
			var edges = net.getEdges();
			synchronized(edges) {
				// Make sure that createEdge cannot lock the things that other threads can lock before calling GetEdgeBetween
				return edges.computeIfAbsent(args, (ends) -> createEdge(root, ends));
			}
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNetwork.getInstance()),
			};
		}
	};
}
