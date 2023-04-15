package io.github.vftdan.wnsirs.core;

import io.github.vftdan.wnsirs.util.*;

public abstract class HandlerMethodDescriptor<THandlerArgs, THandlerRet> extends MethodDescriptor<THandlerArgs, THandlerRet> {
	@Override
	public MethodImplementation<THandlerArgs, THandlerRet> getDefaultImplementation() {
		return null;
	}

	protected HandlerMethodDescriptor(String name, Class<THandlerArgs> argType, Class<THandlerRet> retType) {
		super(name, argType, retType);
	}

	public static abstract class Emitter<TEmitterArgs, TEmitterRet, THandlerArgs, THandlerRet> extends MethodDescriptor<TEmitterArgs, TEmitterRet> {
		public abstract HandlerMethodDescriptor<THandlerArgs, THandlerRet> getHandlerDescriptor();

		protected Emitter(String name, Class<TEmitterArgs> argType, Class<TEmitterRet> retType) {
			super(name, argType, retType);
		}

		public static abstract class Implementation<TEmitterArgs, TEmitterRet, TEmitterState, THandlerArgs, THandlerRet> extends MethodImplementation<TEmitterArgs, TEmitterRet> {
			{
				var descr = implementationFor();
				dependencies = new Dependency[] {Dependency.fromDescriptorMultiplexed(descr.getHandlerDescriptor())};
			}

			public abstract Emitter<? extends TEmitterArgs, ? super TEmitterRet, ? super THandlerArgs, ? extends THandlerRet> implementationFor();

			public TEmitterRet call(AlgorithmPart root, TEmitterArgs args) {
				MutableMonuple<TEmitterState> statePtr = new MutableMonuple<TEmitterState>(getEmptyState(root, args));
				MethodDescriptor<? super THandlerArgs, ? extends THandlerRet> descr = implementationFor().getHandlerDescriptor();
				root.collectAllMethodImplementations(descr, (imp) -> {
					statePtr.value = combineCallImplementation(statePtr.value, (MethodImplementation<? super THandlerArgs, ? extends THandlerRet>) imp, root, getHandlerArgs(statePtr.value, args));
				});
				return extractReturnValue(statePtr.value);
			}

			protected TEmitterState getEmptyState(AlgorithmPart root, TEmitterArgs args) {
				return null;
			}

			protected THandlerArgs getHandlerArgs(TEmitterState state, TEmitterArgs emitterArgs) {
				return (THandlerArgs) emitterArgs;
			}

			protected TEmitterState combineCallImplementation(TEmitterState state, MethodImplementation<? super THandlerArgs, ? extends THandlerRet> imp, AlgorithmPart root, THandlerArgs args) {
				return (TEmitterState) imp.call(root, args);
			}

			protected TEmitterRet extractReturnValue(TEmitterState state) {
				return (TEmitterRet) state;
			}
		}
	}
}
