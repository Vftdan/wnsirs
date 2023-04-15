package io.github.vftdan.wnsirs.core;

import java.util.*;

public class SimpleAlgorithmPart extends AlgorithmPart {
	protected Map<MethodDescriptor<?, ?>, MethodImplementation<?, ?> > methodImplementations = new HashMap();

	@Override
	public <TArgs, TRet> MethodImplementation<TArgs, TRet> getMethodImplementationOrNull(MethodDescriptor<TArgs, TRet> descr) {
		MethodImplementation<TArgs, TRet> imp = (MethodImplementation<TArgs, TRet>) methodImplementations.get(descr);
		return imp;
	}

	protected <TArgs, TRet> void registerImplementation(MethodDescriptor<TArgs, TRet> descr, MethodImplementation<? super TArgs, ? extends TRet> imp) {
		if (imp == null)
			imp = descr.getDefaultImplementation();
		methodImplementations.put(descr, imp);
	}

	protected <TArgs, TRet> void registerImplementation(MethodDescriptor<TArgs, TRet> descr) {
		registerImplementation(descr, null);
	}

	protected <TArgs, TRet> void registerImplementation(MethodImplementation<TArgs, TRet> imp) {
		registerImplementation(imp.implementationFor(), imp);
	}

	@Override
	public Set<MethodDescriptor<?, ?> > getMethods() {
		return methodImplementations.keySet();
	}
}
