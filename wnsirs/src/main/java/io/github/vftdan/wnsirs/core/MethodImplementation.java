package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public abstract class MethodImplementation<TArgs, TRet> {
	protected Dependency[] dependencies = {};
	public abstract MethodDescriptor<? extends TArgs, ? super TRet> implementationFor();
	public abstract TRet call(AlgorithmPart root, TArgs args);
	public Set<Dependency> getDependencies() {
		return new HashSet<Dependency>(Arrays.asList(dependencies));
	}
	public Collection<Lock> getLocks(AlgorithmPart root, TArgs args) {
		return Collections.<Lock>emptyList();
	}

	private static WeakHashMap<AlgorithmPart, Map<MethodDescriptor, Set<Dependency> > > transitiveDependenciesCache = new WeakHashMap();

	public static Set<Dependency> getTransitiveDependencies(AlgorithmPart root, MethodImplementation<?, ?> implementation) {
		Set<Dependency> visited = new HashSet();
		Queue<Dependency> pending = new ArrayDeque<Dependency>();
		pending.addAll(implementation.getDependencies());
		while (!pending.isEmpty()) {
			var dep = pending.remove();
			if (visited.contains(dep))
				continue;
			visited.add(dep);
			var descr = dep.method;
			if (dep.isMultiplexed) {
				root.collectAllMethodImplementations(descr, (imp) -> {
					var deps = imp.getDependencies();
					deps.removeAll(visited);
					pending.addAll(deps);
				});
			} else {
				var imp = root.getMethodImplementation(descr);
				if (imp == null)
					continue;  // We don't expect simulation objects methods to have dependencies
				var deps = imp.getDependencies();
				deps.removeAll(visited);
				pending.addAll(deps);
			}
		}
		return visited;
	}

	public static Set<Dependency> getTransitiveDependencies(AlgorithmPart root, MethodDescriptor descriptor) {
		return getTransitiveDependencies(root, root.getMethodImplementation(descriptor));
	}

	public static <TArgs> Set<Dependency> getCachedInclusiveTransitiveDependencies(AlgorithmPart root, AlgorithmPart cacheKey, MethodDescriptor<TArgs, ?> descriptor, TArgs partialArgs) {
		Map<MethodDescriptor, Set<Dependency> > cache;
		synchronized (transitiveDependenciesCache) {
			cache = transitiveDependenciesCache.computeIfAbsent(cacheKey, (k) -> new ConcurrentHashMap());
		}
		Set<Dependency> exclusive = cache.computeIfAbsent(descriptor, (MethodDescriptor d) -> getTransitiveDependencies(root, d));
		var result = new HashSet<Dependency>(exclusive);
		if (partialArgs == null)
			result.add(Dependency.fromDescriptor(descriptor));
		else
			result.add(new Dependency<TArgs>(descriptor, partialArgs, false));
		return result;
	}

	public static class Dependency<TArgs> {
		protected static final Map<MethodDescriptor, Dependency> instances = new ConcurrentHashMap();
		protected static final Map<MethodDescriptor, Dependency> multiplexedInstances = new ConcurrentHashMap();

		public final MethodDescriptor<TArgs, ?> method;
		public final TArgs partialArgs;
		public final boolean isMultiplexed;
		public Dependency(MethodDescriptor<TArgs, ?> method, TArgs partialArgs, boolean isMultiplexed) {
			this.method = method;
			this.partialArgs = partialArgs;
			this.isMultiplexed = isMultiplexed;
		}

		public static <TArgs> Dependency<TArgs> fromDescriptor(MethodDescriptor<TArgs, ?> method) {
			return instances.computeIfAbsent(method, (m) -> new Dependency<TArgs>((MethodDescriptor<TArgs, ?>) m, null, false));
		}

		public static <TArgs> Dependency<TArgs> fromDescriptorMultiplexed(MethodDescriptor<TArgs, ?> method) {
			return multiplexedInstances.computeIfAbsent(method, (m) -> new Dependency<TArgs>((MethodDescriptor<TArgs, ?>) m, null, true));
		}

		@Override
		public int hashCode() {
			var h = this.method.hashCode();
			if (this.partialArgs != null)
				h ^= this.partialArgs.hashCode();
			return h;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (other == null)
				return false;
			if (!(other instanceof Dependency))
				return false;
			Dependency theOther = (Dependency) other;
			return Objects.equals(this.method, theOther.method) && Objects.equals(this.partialArgs, theOther.partialArgs) && (this.isMultiplexed == theOther.isMultiplexed);
		}

		public Collection<Lock> getImplementationLocks(AlgorithmPart root) {
			if (isMultiplexed) {
				Set<Lock> locks = new HashSet<Lock>();
				root.collectAllMethodImplementations(method, (imp) -> {
					locks.addAll(imp.getLocks(root, partialArgs));
				});
				return locks;
			}
			MethodImplementation<TArgs, ?> imp = root.getMethodImplementation(method);
			if (imp == null) {
				System.err.println("Implementation of " + method + " is null");
			}
			return imp.getLocks(root, partialArgs);
		}
	}
}
