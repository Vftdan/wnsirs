package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class AntCloned extends HandlerMethodDescriptor<Void, Void> {
	protected static AntCloned instance = new AntCloned();

	protected AntCloned() {
		super("antCloned", Void.class, Void.class);
	}

	public static AntCloned getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<Void, Void, Void, Void> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super("emitAntCloned", Void.class, Void.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<Void, Void> getHandlerDescriptor() {
			return AntCloned.getInstance();
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
		}
	}
}
