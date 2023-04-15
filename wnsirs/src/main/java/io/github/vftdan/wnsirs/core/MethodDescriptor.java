package io.github.vftdan.wnsirs.core;

public abstract class MethodDescriptor<TArgs, TRet> {
	protected Class<TArgs> argType;
	protected Class<TRet> retType;
	protected String name;

	protected MethodDescriptor(String name, Class<TArgs> argType, Class<TRet> retType) {
		this.name = name;
		this.argType = argType;
		this.retType = retType;
	}

	protected MethodDescriptor() {}

	public String getName() {
		return name;
	}
	public Class<TArgs> getArgType() {
		return argType;
	}
	public Class<TRet> getRetType() {
		return retType;
	}

	public abstract MethodImplementation<TArgs, TRet> getDefaultImplementation();

	@Override
	public String toString() {
		return getClass() + "{" + name + ": " + argType + " -> " + retType + "}";
	}
}
