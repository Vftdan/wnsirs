package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class CompositeAlgorithmPart extends AlgorithmPart {
	protected Map<MethodDescriptor<?, ?>, List<String>> possibleImplementationChildren = new ConcurrentHashMap<MethodDescriptor<?, ?>, List<String>>();
	protected Map<String, AlgorithmPart> parts = new HashMap<String, AlgorithmPart>();
	protected Set<MethodDescriptor<?, ?> > methodsCache = null;

	protected Set<MethodDescriptor<?, ?> > getMethodsUncached() {
		var methods = new HashSet<MethodDescriptor<?, ?> >();
		for (var possible: possibleImplementationChildren.entrySet()) {
			if (possible.getValue().size() > 0)
				methods.add(possible.getKey());
		}
		return methods;
	}

	@Override
	public Set<MethodDescriptor<?, ?> > getMethods() {
		if (methodsCache == null)
			methodsCache = getMethodsUncached();
		return Collections.unmodifiableSet(methodsCache);
	}

	@Override
	public <TArgs, TRet> MethodImplementation<TArgs, TRet> getMethodImplementationOrNull(MethodDescriptor<TArgs, TRet> descr) {
		AlgorithmPart part = null;
		List<String> possible = possibleImplementationChildren.get(descr);
		if (possible != null && possible.size() > 0) {
			part = getPart(possible.get(0));
		}
		if (part == null) {
			return null;
		}
		return part.getMethodImplementationOrNull(descr);
	}

	@Override
	public <TArgs, TRet> void collectAllMethodImplementations(MethodDescriptor<TArgs, TRet> descr, Consumer<MethodImplementation<? super TArgs, ? extends TRet> > callback) {
		List<String> possible = possibleImplementationChildren.get(descr);
		if (possible == null)
			return;
		for (var key: possible) {
			var part = getPart(key);
			if (part != null)
				part.collectAllMethodImplementations(descr, callback);
		}
	}

	public String setPart(AlgorithmPart part) {
		return setPart(null, part);
	}

	public String setPart(String name, AlgorithmPart part) {
		if (name == null)
			name = part.getDefaultRole();
		methodsCache = null;
		if (parts.containsKey(name))
			delPart(name);
		parts.put(name, part);
		for (var descr: part.getMethods()) {
			var possible = possibleImplementationChildren.computeIfAbsent(descr, (d) -> {return new LinkedList<String>();});
			possible.add(0, name);
		}
		return name;
	}

	public AlgorithmPart getPart(String name) {
		return parts.get(name);
	}

	public void delPart(String name) {
		var part = getPart(name);
		if (part == null)
			return;
		methodsCache = null;
		for (var descr: part.getMethods()) {
			var possible = possibleImplementationChildren.get(descr);
			if (possible == null)
				continue;
			possible.remove(name);
		}
		parts.remove(name);
	}
}
