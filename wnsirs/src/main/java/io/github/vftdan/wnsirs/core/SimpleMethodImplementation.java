package io.github.vftdan.wnsirs.core;

import java.util.function.BiFunction;

public class SimpleMethodImplementation<TArgs, TRet> extends MethodImplementation<TArgs, TRet> {
	protected MethodDescriptor<? extends TArgs, ? super TRet> descriptor;
	protected BiFunction<AlgorithmPart, TArgs, TRet> function;

	public SimpleMethodImplementation(MethodDescriptor<? extends TArgs, ? super TRet> descriptor, BiFunction<AlgorithmPart, TArgs, TRet> func, MethodImplementation.Dependency... deps) {
		this.descriptor = descriptor;
		this.function = func;
		this.dependencies = deps;
	}

	public SimpleMethodImplementation(MethodDescriptor<? extends TArgs, ? super TRet> descriptor, BiFunction<AlgorithmPart, TArgs, TRet> func, MethodDescriptor... deps) {
		this.descriptor = descriptor;
		this.function = func;
		var depsObjects = new MethodImplementation.Dependency[deps.length];
		for (int i = 0; i < deps.length; ++i)
			depsObjects[i] = MethodImplementation.Dependency.fromDescriptor(deps[i]);
		this.dependencies = depsObjects;
	}

	@Override
	public MethodDescriptor<? extends TArgs, ? super TRet> implementationFor() {
		return descriptor;
	}

	@Override
	public TRet call(AlgorithmPart root, TArgs args) {
		return function.apply(root, args);
	}
}
