package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class LaunchAnt extends MethodDescriptor<Node, Void> {
	protected static LaunchAnt instance = new LaunchAnt();

	protected LaunchAnt() {
		super("launchAnt", Node.class, Void.class);
	}

	public static LaunchAnt getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Node, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Node, Void> defaultImplementation = new Implementation();

	public static class Implementation extends MethodImplementation<Node, Void> {
		@Override
		public MethodDescriptor<? extends Node, ? super Void> implementationFor() {
			return LaunchAnt.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Node args) {
			Context ctx;
			try {
				ctx = (Context) root;
			} catch (ClassCastException e) {
				return null;
			}
			var ant = createAnt(ctx);
			ctx.setPart(ant);
			ctx.callMethod(SetTargetNode.getInstance(), args);  // Shouldn't create race conditions, because ant is expected to be owned exclusively
			ctx.callMethod(UpdateHistory.getInstance(), ctx.callMethod(GetNode.getInstance(), null));
			ctx.callMethod(TransitionAnt.getInstance(), null);
			return null;
		}

		protected Ant createAnt(Context ctx) {
			return new Ant();
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(SetTargetNode.getInstance()),
				Dependency.fromDescriptor(GetNode.getInstance()),
				Dependency.fromDescriptor(UpdateHistory.getInstance()),
				Dependency.fromDescriptor(TransitionAnt.getInstance()),
			};
		}
	};
}
