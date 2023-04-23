package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class EdgeCreated extends HandlerMethodDescriptor<Edge, Void> {
	protected static EdgeCreated instance = new EdgeCreated();

	protected EdgeCreated() {
		super("edgeCreated", Edge.class, Void.class);
	}

	public static EdgeCreated getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<Edge, Void, Edge, Void> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super("emitEdgeCreated", Edge.class, Void.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<Edge, Void> getHandlerDescriptor() {
			return EdgeCreated.getInstance();
		}

		@Override
		public MethodImplementation<Edge, Void> getDefaultImplementation() {
			if (defaultImplementation == null)
				defaultImplementation = new Implementation();
			return defaultImplementation;
		}

		protected Implementation defaultImplementation = null;

		public static class Implementation extends HandlerMethodDescriptor.Emitter.Implementation<Edge, Void, Void, Edge, Void> {
			@Override
			public io.github.vftdan.wnsirs.core.HandlerMethodDescriptor.Emitter<? extends Edge, ? super Void, ? super Edge, ? extends Void> implementationFor() {
				return Emitter.getInstance();
			}
		}
	}
}
