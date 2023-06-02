package io.github.vftdan.wnsirs.algorithms;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;

import javafx.util.Pair;

public class CenterAndTargetDistanceAwareHeuristic extends StandardHeuristic {
	{
		name = "Center and target distance aware heuristic formula";
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
			var center = root.callMethod(GetCenterNodePosition.getInstance());
			if (currentNode == otherNode)
				otherNode = edge.getStart();
			var currentPosition = currentNode.getPosition();
			var otherPosition = otherNode.getPosition();
			var targetPosition = targetNode.getPosition();
			Double remainingDistance = root.callMethod(CalculateDistance.getInstance(), new Pair(otherPosition, targetPosition));
			var targetDistanceHeuristicWeight = root.callMethod(GetTargetDistanceHeuristicWeight.getInstance());
			var centerDistanceHeuristicWeight = root.callMethod(GetCenterDistanceHeuristicWeight.getInstance());
			if (centerDistanceHeuristicWeight != 0.0)  // For more uniform network usage we encourage to increase the distance from the center
				remainingDistance -= Math.min(remainingDistance, (
							(Double) root.callMethod(CalculateDistance.getInstance(), new Pair(otherPosition, center)) -
							(Double) root.callMethod(CalculateDistance.getInstance(), new Pair(currentPosition, center))
						) * centerDistanceHeuristicWeight);
			return 1.0 / (hopDistance * (1 - targetDistanceHeuristicWeight) + remainingDistance * targetDistanceHeuristicWeight + 0.0001);
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetEdgeLength.getInstance()),
				Dependency.fromDescriptor(GetEdge.getInstance()),
				Dependency.fromDescriptor(GetNode.getInstance()),
				Dependency.fromDescriptor(GetTargetNode.getInstance()),
				Dependency.fromDescriptor(GetCenterNodePosition.getInstance()),
				Dependency.fromDescriptor(CalculateDistance.getInstance()),
				Dependency.fromDescriptor(GetTargetDistanceHeuristicWeight.getInstance()),
			};
		}
	};
}
