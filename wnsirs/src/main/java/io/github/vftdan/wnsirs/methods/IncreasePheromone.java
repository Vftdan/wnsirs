package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class IncreasePheromone extends HandlerMethodDescriptor<Void, Void> {
	protected static IncreasePheromone instance = new IncreasePheromone();

	protected IncreasePheromone() {
		super("increasePheromone", Void.class, Void.class);
	}

	public static IncreasePheromone getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<Void, Void, Void, Void> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super("emitIncreasePheromone", Void.class, Void.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<Void, Void> getHandlerDescriptor() {
			return IncreasePheromone.getInstance();
		}

		@Override
		public MethodImplementation<Void, Void> getDefaultImplementation() {
			if (defaultImplementation == null)
				defaultImplementation = new Implementation();
			return defaultImplementation;
		}

		protected Implementation defaultImplementation = null;

		public static class Implementation extends HandlerMethodDescriptor.Emitter.Implementation<Void, Void, Void, Void, Void> {
			@Override
			public io.github.vftdan.wnsirs.core.HandlerMethodDescriptor.Emitter<? extends Void, ? super Void, ? super Void, ? extends Void> implementationFor() {
				return Emitter.getInstance();
			}

			@Override
			public Void call(AlgorithmPart root, Void args) {
				super.call(root, args);
				double pheromone = root.callMethod(GetPheromone.getInstance());
				pheromone += root.callMethod(GetAntDeltaPheromone.getInstance());
				root.callMethod(SetPheromone.getInstance(), pheromone);
				return null;
			}

			{
				var oldDependencies = dependencies;
				dependencies = new Dependency[oldDependencies.length + 3];
				for (int i = 0; i < oldDependencies.length; ++i)
					dependencies[i] = oldDependencies[i];
				dependencies[oldDependencies.length + 0] = Dependency.fromDescriptor(GetPheromone.getInstance());
				dependencies[oldDependencies.length + 1] = Dependency.fromDescriptor(GetAntDeltaPheromone.getInstance());
				dependencies[oldDependencies.length + 2] = Dependency.fromDescriptor(SetPheromone.getInstance());
			}
		}
	}
}
