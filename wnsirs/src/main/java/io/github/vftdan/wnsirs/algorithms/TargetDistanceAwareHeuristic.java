package io.github.vftdan.wnsirs.algorithms;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;

import javafx.util.Pair;

public class TargetDistanceAwareHeuristic extends StandardHeuristic {
	{
		name = "Target distance aware heuristic formula";
		registerImplementation(new GetHeuristicImplementation());
	}

	public static class GetHeuristicImplementation extends MethodImplementation<Void, Double> {
		@Override
		public MethodDescriptor<? extends Void, ? super Double> implementationFor() {
			return GetHeuristic.getInstance();
		}

		@Override
		public Double call(AlgorithmPart root, Void args) {
			var hopDistance = root.callMethod(GetEdgeLength.getInstance());
			var edge = root.callMethod(GetEdge.getInstance());
			var currentNode = root.callMethod(GetNode.getInstance());
			var targetNode = root.callMethod(GetTargetNode.getInstance());
			var otherNode = edge.getEnd();
			if (currentNode == otherNode)
				otherNode = edge.getStart();
			Double remainingDistance = root.callMethod(CalculateDistance.getInstance(), new Pair(otherNode.getPosition(), targetNode.getPosition()));
			var targetDistanceHeuristicWeight = root.callMethod(GetTargetDistanceHeuristicWeight.getInstance());
			return 1.0 / (hopDistance * (1 - targetDistanceHeuristicWeight) + remainingDistance * targetDistanceHeuristicWeight + 0.0001);
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetEdgeLength.getInstance()),
				Dependency.fromDescriptor(GetEdge.getInstance()),
				Dependency.fromDescriptor(GetNode.getInstance()),
				Dependency.fromDescriptor(GetTargetNode.getInstance()),
				Dependency.fromDescriptor(CalculateDistance.getInstance()),
				Dependency.fromDescriptor(GetTargetDistanceHeuristicWeight.getInstance()),
			};
		}
	};
}
