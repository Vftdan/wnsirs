package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

public class CLASS_NAME extends HandlerMethodDescriptor<TArgs, TRet> {
	protected static CLASS_NAME instance = new CLASS_NAME();

	protected CLASS_NAME() {
		super(HANDLER_NAME, TArgs.class, TRet.class);
	}

	public static CLASS_NAME getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<TArgs, TRet, TArgs, TRet> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super(EMITTER_NAME, TArgs.class, TRet.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<TArgs, TRet> getHandlerDescriptor() {
			return CLASS_NAME.getInstance();
		}

		@Override
		public MethodImplementation<TArgs, TRet> getDefaultImplementation() {
			if (defaultImplementation == null)
				defaultImplementation = new Implementation();
			return defaultImplementation;
		}

		protected Implementation defaultImplementation = null;

		public static class Implementation extends HandlerMethodDescriptor.Emitter.Implementation<TArgs, TRet, TRet, TArgs, TRet> {
			@Override
			public io.github.vftdan.wnsirs.core.HandlerMethodDescriptor.Emitter<? extends TArgs, ? super TRet, ? super TArgs, ? extends TRet> implementationFor() {
				return Emitter.getInstance();
			}
		}
	}
}
