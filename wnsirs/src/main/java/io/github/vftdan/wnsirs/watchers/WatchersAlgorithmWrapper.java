package io.github.vftdan.wnsirs.watchers;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.function.Consumer;

public class WatchersAlgorithmWrapper extends Algorithm {
	protected CompositeAlgorithmPart wrapped;
	protected CompositeAlgorithmPart watchers = new CompositeAlgorithmPart();

	public WatchersAlgorithmWrapper(CompositeAlgorithmPart wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public Set<MethodDescriptor<?, ?> > getMethods() {
		var set = new HashSet<MethodDescriptor<?, ?> >(wrapped.getMethods());
		set.addAll(watchers.getMethods());
		return set;
	}

	@Override
	public <TArgs, TRet> MethodImplementation<TArgs, TRet> getMethodImplementationOrNull(MethodDescriptor<TArgs, TRet> descr) {
		var imp = wrapped.getMethodImplementation(descr);
		var watcherImp = watchers.getMethodImplementationOrNull(descr);
		if (imp == null)
			return watcherImp;
		if (watcherImp == null)
			return imp;
		return WatchedMethodImplementation.from(imp, watcherImp);
	}

	@Override
	public <TArgs, TRet> void collectAllMethodImplementations(MethodDescriptor<TArgs, TRet> descr, Consumer<MethodImplementation<? super TArgs, ? extends TRet> > callback) {
		var watcherImp = watchers.getMethodImplementation(descr);
		if (watcherImp == null) {
			wrapped.collectAllMethodImplementations(descr, callback);
			return;
		}
		if (wrapped.getMethodImplementationOrNull(descr) == null)
			callback.accept(watcherImp);
		wrapped.collectAllMethodImplementations(descr, (imp) -> {
			callback.accept(WatchedMethodImplementation.from((MethodImplementation<TArgs, TRet>) imp, watcherImp));
		});
	}

	@Override
	public String setPart(String name, AlgorithmPart part) {
		return wrapped.setPart(name, part);
	}

	@Override
	public AlgorithmPart getPart(String name) {
		return wrapped.getPart(name);
	}

	@Override
	public void delPart(String name) {
		wrapped.delPart(name);
	}

	public String setWatcher(String name, BaseWatcher part) {
		return watchers.setPart(name, part);
	}

	public String setWatcher(BaseWatcher part) {
		return watchers.setPart(part);
	}

	public AlgorithmPart getWatcher(String name) {
		return watchers.getPart(name);
	}

	public void delWatcher(String name) {
		watchers.delPart(name);
	}

	public static class BaseWatcher extends SimpleAlgorithmPart {
		{
			defaultRole = "watcher";
		}
	}

	public static class WatchedMethodImplementation<TArgs, TRet> extends MethodImplementation<TArgs, TRet> {
		protected static final Map<Pair, WatchedMethodImplementation> instances = new ConcurrentHashMap();
		protected MethodImplementation<TArgs, TRet> imp, watcherImp;

		public WatchedMethodImplementation(MethodImplementation<TArgs, TRet> imp, MethodImplementation<TArgs, TRet> watcherImp) {
			this.imp = imp;
			this.watcherImp = watcherImp;
		}

		public static <TArgs, TRet> WatchedMethodImplementation<TArgs, TRet> from(MethodImplementation<TArgs, TRet> imp, MethodImplementation<TArgs, TRet> watcherImp) {
			return ((Map<Pair<TArgs, TRet>, WatchedMethodImplementation<TArgs, TRet>>) (Map) instances).computeIfAbsent(new Pair(imp, watcherImp), (p) -> new WatchedMethodImplementation<TArgs, TRet>(imp, watcherImp));
		}

		@Override
		public MethodDescriptor<? extends TArgs, ? super TRet> implementationFor() {
			return this.imp.implementationFor();
		}

		@Override
		public TRet call(AlgorithmPart root, TArgs args) {
			watcherImp.call(root, args);
			return imp.call(root, args);
		}

		@Override
		public Set<Dependency> getDependencies() {
			var set = new HashSet<Dependency>(watcherImp.getDependencies());
			set.addAll(imp.getDependencies());
			return set;
		}

		@Override
		public Collection<Lock> getLocks(AlgorithmPart root, TArgs args) {
			var list = new ArrayList<Lock>(watcherImp.getLocks(root, args));
			list.addAll(imp.getLocks(root, args));
			return list;
		}
	}
}
