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
			var visited = root.callMethod(GetNodeHistory.getInstance());
			var total = root.callMethod(GetNetwork.getInstance()).getNodes();
			// src: T. Camilo, C. Carreto, J. S. Silva, and F. Boavida, "An energy-efficient ant-based routing algorithm for wireless sensor networks"; eq. 2
			// Seems to prefer ants that visited more nodes, may be a mistake
			root.callMethod(SetAntDeltaPheromone.getInstance(), root.callMethod(GetPheromoneStrength.getInstance()) / (
				Math.max(1, total.size() - visited.length)
			));
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNodeHistory.getInstance()),
				Dependency.fromDescriptor(GetNetwork.getInstance()),
				Dependency.fromDescriptor(SetAntDeltaPheromone.getInstance()),
				Dependency.fromDescriptor(GetPheromoneStrength.getInstance()),
			};
		}
	};
}
