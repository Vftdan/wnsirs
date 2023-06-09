package io.github.vftdan.wnsirs.core;

import java.util.*;

import io.github.vftdan.wnsirs.methods.GetNode;

public class Node extends SimulationObject {
	{
		defaultRole = "node";
		registerImplementation(GetNode.getInstance(), new MethodImplementation<Void, Node>() {
			@Override
			public MethodDescriptor<? extends Void, ? super Node> implementationFor() {
				return GetNode.getInstance();
			}

			@Override
			public Node call(AlgorithmPart root, Void args) {
				return Node.this;
			}
		});
	}

	public Collection<Node> neighbors = new ArrayList<Node>();

	protected double[] position;

	public double[] getPosition() {
		return position;
	}

	public Node(double[] position) {
		this.position = position;
	}

	@Override
	public String toString() {
		var position = this.position;
		if (position == null)
			return super.toString();
		var builder = new StringBuilder();
		builder.append("Node{");
		for (int i = 0; i < position.length; ++i) {
			if (i > 0)
				builder.append(", ");
			builder.append(position[i]);
		}
		builder.append("}");
		return builder.toString();
	}
}
