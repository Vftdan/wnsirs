package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import javafx.util.Pair;

public class EvaluateAnt extends MethodDescriptor<Void, Void> {
	private static EvaluateAnt instance = new EvaluateAnt();

	public static EvaluateAnt getInstance() {
		return instance;
	}

	protected EvaluateAnt() {
		super("evaluateAnt", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
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
			double fitness = 1 / totalDist;  // by default
			if (!Double.isFinite(fitness))
				fitness = Double.MAX_VALUE;
			root.callMethod(SetAntFitness.getInstance(), fitness);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(SetAntFitness.getInstance()),
			};
		}
	};
}
