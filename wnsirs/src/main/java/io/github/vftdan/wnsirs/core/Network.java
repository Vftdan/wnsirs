package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.github.vftdan.wnsirs.methods.GetNetwork;
import javafx.util.Pair;

public class Network extends SimulationObject {
	{
		defaultRole = "network";
		registerImplementation(GetNetwork.getInstance(), new MethodImplementation<Void, Network>() {
			@Override
			public MethodDescriptor<? extends Void, ? super Network> implementationFor() {
				return GetNetwork.getInstance();
			}

			@Override
			public Network call(AlgorithmPart root, Void args) {
				return Network.this;
			}
		});
	}

	protected Map<Pair<Node, Node>, Edge> edges = new ConcurrentHashMap();
	protected Set<Node> nodes = Collections.newSetFromMap(new WeakHashMap<Node, Boolean>());

	public Map<Pair<Node, Node>, Edge> getEdges() {
		return edges;
	}

	public Set<Node> getNodes() {
		return nodes;
	}
}
