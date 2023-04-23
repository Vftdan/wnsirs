package io.github.vftdan.wnsirs.core;

import java.util.*;

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

	protected Map<Pair<Node, Node>, Edge> edges = new HashMap();

	public Map<Pair<Node, Node>, Edge> getEdges() {
		return edges;
	}
}
