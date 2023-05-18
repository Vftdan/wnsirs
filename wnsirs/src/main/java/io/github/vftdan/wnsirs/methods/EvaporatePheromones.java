package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Context.GetScheduler;

public class EvaporatePheromones extends MethodDescriptor<Void, Void> {
	private static EvaporatePheromones instance = new EvaporatePheromones();

	public static EvaporatePheromones getInstance() {
		return instance;
	}

	protected EvaporatePheromones() {
		super("evaporatePheromones", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return EvaporatePheromones.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			var network = root.callMethod(GetNetwork.getInstance());
			var edges = network.getEdges();
			var ctx = (Context) root;
			for (var edge: edges.values()) {
				var newCtx = ctx.clone();
				newCtx.setPart(edge);
				ctx.callMethod(GetScheduler.getInstance()).scheduleTask(0, newCtx, EvaporateCurrentPheromone.getInstance(), null);
			}
			var newCtx = ctx.clone();
			newCtx.delPart("edge");
			ctx.callMethod(GetScheduler.getInstance()).scheduleTask(0, newCtx, EvaporateCurrentPheromone.getInstance(), null);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNetwork.getInstance()),
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
		}
	};
}
