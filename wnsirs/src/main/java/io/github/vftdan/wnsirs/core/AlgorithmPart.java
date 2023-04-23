package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.function.Consumer;

public abstract class AlgorithmPart {
	protected String defaultRole = "none";

	public Set<MethodDescriptor<?, ?> > getMethods() {
		return new HashSet<MethodDescriptor<?, ?> >();
	}

	public abstract <TArgs, TRet> MethodImplementation<TArgs, TRet> getMethodImplementationOrNull(MethodDescriptor<TArgs, TRet> descr);

	public <TArgs, TRet> MethodImplementation<TArgs, TRet> getMethodImplementation(MethodDescriptor<TArgs, TRet> descr) {
		var imp = getMethodImplementationOrNull(descr);
		if (imp == null)
			return getDefaultMethodImplementation(descr);
		return imp;
	}

	public <TArgs, TRet> void collectAllMethodImplementations(MethodDescriptor<TArgs, TRet> descr, Consumer<MethodImplementation<? super TArgs, ? extends TRet> > callback) {
		var imp = getMethodImplementationOrNull(descr);
		if (imp == null)
			return;
		callback.accept(imp);
	}

	public <TArgs, TRet> TRet callMethod(AlgorithmPart root, MethodDescriptor<TArgs, TRet> descr, TArgs args) {
		return getMethodImplementation(descr).call(root, args);
	}

	public <TArgs, TRet> TRet callMethod(MethodDescriptor<TArgs, TRet> descr, TArgs args) {
		return callMethod(this, descr, args);
	}

	public <TRet> TRet callMethod(MethodDescriptor<Void, TRet> descr) {
		return callMethod(descr, null);
	}

	protected <TArgs, TRet> MethodImplementation<TArgs, TRet> getDefaultMethodImplementation(MethodDescriptor<TArgs, TRet> descr) {
		return descr.getDefaultImplementation();
	}

	public String getDefaultRole() {
		return defaultRole;
	}
}
