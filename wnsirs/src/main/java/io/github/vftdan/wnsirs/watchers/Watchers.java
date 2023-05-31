package io.github.vftdan.wnsirs.watchers;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.*;

public class Watchers {
	public static final StoredValueChangeWatcher<Double> pheromoneChangeWatcher = new StoredValueChangeWatcher<Double>(SetPheromone.getInstance(), "pheromone") {{
		var watcher = this;
		registerImplementation(new MethodImplementation<Double, Void>() {
			@Override
			public MethodDescriptor<? extends Double, ? super Void> implementationFor() {
				return watcher.getHandlerDescriptor();
			}

			@Override
			public Void call(AlgorithmPart root, Double args) {
				if (!(root instanceof Context))
					return null;
				var ctx = (Context) root;
				var edge = root.callMethod(GetEdge.getInstance());
				ctx.getScheduler().showUserMessage("Set pheromone of " + (edge == null ? "remaining edges" : edge) + " to " + args);
				return null;
			}
		});
	}};
}
