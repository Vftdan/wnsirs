package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Context.GetScheduler;

public class ReceiveAnt extends MethodDescriptor<Void, Void> {
	private static ReceiveAnt instance = new ReceiveAnt();

	public static ReceiveAnt getInstance() {
		return instance;
	}

	protected ReceiveAnt() {
		super("receiveAnt", Void.class, Void.class);
	}

	@Override
	public MethodImplementation<Void, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	private MethodImplementation<Void, Void> defaultImplementation = new MethodImplementation<Void, Void>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Void> implementationFor() {
			return ReceiveAnt.getInstance();
		}

		@Override
		public Void call(AlgorithmPart root, Void args) {
			var ctx = (Context) root;
			var node = root.callMethod(GetNextNode.getInstance(), null);
			ctx.callMethod(UpdateHistory.getInstance(), node);
			ctx.setPart(ctx.callMethod(GetNextNode.getInstance(), null));
			ctx.callMethod(SetNextNode.getInstance(), null);
			MethodDescriptor<?, ?> nextAction = TransitionAnt.getInstance();
			if (ctx.callMethod(HasArrived.getInstance()))
				nextAction = AntArrived.getInstance();
			ctx.callMethod(GetScheduler.getInstance(), null).scheduleTask(0, ctx, nextAction, null);
			return null;
		}

		{
			dependencies = new Dependency[] {
				// Dependency.fromDescriptor(GetNextNode.getInstance()),  // FIXME create a ReentrantReadWriteLock that allows the same thread to have both read and write lock?
				Dependency.fromDescriptor(UpdateHistory.getInstance()),
				Dependency.fromDescriptor(SetNextNode.getInstance()),
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
		}
	};
}
