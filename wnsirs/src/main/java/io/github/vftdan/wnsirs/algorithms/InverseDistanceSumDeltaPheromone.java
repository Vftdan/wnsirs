package io.github.vftdan.wnsirs.algorithms;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;

import javafx.util.Pair;

public class InverseDistanceSumDeltaPheromone extends StandardDeltaPheromone {
	{
		name = "Inverse distance sum delta pheromone formula";
		registerImplementation(new EvaluateAntImplementation());
	}

	public static class EvaluateAntImplementation extends MethodImplementation<Void, Void> {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return EvaluateAnt.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			// TODO separate function for the list of history edges
			var list = root.callMethod(GetNodeHistory.getInstance());
			Node u = null;
			var ctx = (Context) root;
			double totalDist = 0;
			for (var listNode: list) {
				Node v = listNode.value;
				try {
					if (u == null || v == null)
						continue;
					var edge = root.callMethod(GetEdgeBetween.getInstance(), new Pair<Node, Node>(u, v));
					ctx.setPart(edge);
					double dist = root.callMethod(GetEdgeLength.getInstance());
					totalDist += dist;
				} finally {
					u = v;
					ctx.delPart("edge");
				}
			}
			double deltaPheromone = root.callMethod(GetPheromoneStrength.getInstance()) / totalDist;
			if (!Double.isFinite(deltaPheromone))
				deltaPheromone = Double.MAX_VALUE;
			root.callMethod(SetAntDeltaPheromone.getInstance(), deltaPheromone);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(SetAntDeltaPheromone.getInstance()),
				Dependency.fromDescriptor(GetPheromoneStrength.getInstance()),
			};
		}
	};
}
