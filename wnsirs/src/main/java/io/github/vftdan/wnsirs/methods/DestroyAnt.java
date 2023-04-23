package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class DestroyAnt extends HandlerMethodDescriptor<Void, Void> {
	protected static DestroyAnt instance = new DestroyAnt();

	protected DestroyAnt() {
		super("destroyAnt", Void.class, Void.class);
	}

	public static DestroyAnt getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<Void, Void, Void, Void> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super("emitDestroyAnt", Void.class, Void.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<Void, Void> getHandlerDescriptor() {
			return DestroyAnt.getInstance();
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
