package io.github.vftdan.wnsirs.watchers;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;

public class CallMethodWatcher<T> extends WatchersAlgorithmWrapper.BaseWatcher {
	protected MethodDescriptor<T, ?> watchedDescriptor;
	protected CallMethodHandler<T, ?> handlerMethod;
	protected MethodImplementation<T, ?> handlerMethodImplementation;
	public CallMethodWatcher(MethodDescriptor<T, ?> watchedDescriptor) {
		this.watchedDescriptor = watchedDescriptor;
		this.defaultRole = "callMethodWatcher_" + watchedDescriptor.getName();
		this.handlerMethod = createHandlerMethod();
		if (this.handlerMethod != null) {
			registerImplementation((MethodDescriptor) watchedDescriptor, (MethodImplementation) createEmitterMethodImplementation());
		}
	}

	public CallMethodHandler.Emitter<T, ?> getEmitterDescriptor() {
		return handlerMethod.emitter;
	}

	public CallMethodHandler<T, ?> getHandlerDescriptor() {
		return handlerMethod;
	}

	protected CallMethodHandler<T, ?> createHandlerMethod() {
		return new CallMethodHandler("watchCall_" + watchedDescriptor.getName(), watchedDescriptor.getArgType(), watchedDescriptor.getRetType(), "emitWatchCall_" + watchedDescriptor.getName());
	}

	protected MethodImplementation<T, ?> createEmitterMethodImplementation() {
		return getEmitterDescriptor().getDefaultImplementation();
	}

	public static class CallMethodHandler<TArgs, TRet> extends HandlerMethodDescriptor<TArgs, TRet> {
		protected Emitter<TArgs, TRet> emitter;

		protected CallMethodHandler(String name, Class<TArgs> argType, Class<TRet> retType, String emitterName) {
			super(name, argType, retType);
			createEmitter(emitterName);
		}

		public static abstract class Emitter<TArgs, TRet> extends HandlerMethodDescriptor.Emitter<TArgs, TRet, TArgs, TRet> {
			protected Emitter(String name, Class<TArgs> argType, Class<TRet> retType) {
				super(name, argType, retType);
			}

			public static abstract class Implementation<TArgs, TRet> extends HandlerMethodDescriptor.Emitter.Implementation<TArgs, TRet, Object, TArgs, TRet> {
			}
		}

		private MethodImplementation<TArgs, TRet> defaultImplementation = null;
		protected void createEmitter(String emitterName) {
			var handler = this;
			var emitter = new Emitter<TArgs, TRet>(emitterName, getArgType(), getRetType()) {
				@Override
				public HandlerMethodDescriptor<TArgs, TRet> getHandlerDescriptor() {
					return handler;
				}

				@Override
				public MethodImplementation<TArgs, TRet> getDefaultImplementation() {
					return defaultImplementation;
				}
			};
			defaultImplementation = new Emitter.Implementation<TArgs, TRet>() {
				@Override
				public HandlerMethodDescriptor.Emitter<? extends TArgs, ? super TRet, ? super TArgs, ? extends TRet> implementationFor() {
					return emitter;
				}
			};
			this.emitter = emitter;
		}
	}
}
