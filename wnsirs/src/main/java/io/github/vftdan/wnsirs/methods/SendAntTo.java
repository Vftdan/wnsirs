package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Context.GetScheduler;

public class SendAntTo extends MethodDescriptor<Node, Void> {
	protected static SendAntTo instance = new SendAntTo();

	protected SendAntTo() {
		super("sendAntTo", Node.class, Void.class);
	}

	public static SendAntTo getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Node, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Node, Void> defaultImplementation = new MethodImplementation<Node, Void>() {
		@Override
		public MethodDescriptor<? extends Node, ? super Void> implementationFor() {
			return instance;
		}

		@Override
		public Void call(AlgorithmPart root, Node args) {
			var ctx = (Context) root;
			// if this ant has pending => clone the ant
			if (ctx.callMethod(GetNextNode.getInstance(), null) != null) {
				var ant = new Ant();
				ctx.callMethod(GetAnt.getInstance(), null).getValueStorage().copyTo(ant.getValueStorage());
				ctx.setPart(ant);
				ctx.callMethod(AntCloned.Emitter.getInstance(), null);
			}
			ctx.callMethod(SetNextNode.getInstance(), args);
			double delay = ctx.callMethod(GetTransmissionDelay.getInstance(), null);
			// clone the context
			var newCtx = ctx.clone();
			// schedule ant reception
			ctx.callMethod(GetScheduler.getInstance(), null).scheduleTask(delay, newCtx, ReceiveAnt.getInstance(), null);
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetNextNode.getInstance()),
				Dependency.fromDescriptor(GetAnt.getInstance()),
				Dependency.fromDescriptor(AntCloned.Emitter.getInstance()),
				Dependency.fromDescriptor(SetNextNode.getInstance()),
				Dependency.fromDescriptor(GetTransmissionDelay.getInstance()),
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
		}
	};
}
