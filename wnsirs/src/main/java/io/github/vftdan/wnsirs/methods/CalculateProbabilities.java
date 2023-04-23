package io.github.vftdan.wnsirs.methods;

import java.util.*;
import java.util.concurrent.locks.*;

import io.github.vftdan.wnsirs.core.*;

public class CalculateProbabilities extends MethodDescriptor<Void, List<Double> > {
	protected static CalculateProbabilities instance = new CalculateProbabilities();

	protected CalculateProbabilities() {
		super("calculateProbabilities", Void.class, (Class<List<Double> >)(Class<?>)List.class);
	}

	public static CalculateProbabilities getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, List<Double> > getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, List<Double> > defaultImplementation = new MethodImplementation<Void, List<Double> >() {
		@Override
		public MethodDescriptor<? extends Void, ? super List<Double> > implementationFor() {
			return instance;
		}

		@Override
		public List<Double>  call(AlgorithmPart root, Void args) {
			ArrayList<Double> probas = new ArrayList<Double>();
			var ctx = (Context) root;
			var oldEdge = root.callMethod(GetEdge.getInstance());
			try {
				for (var edge: getConsideredEdges(root)) {
					ctx.setPart(edge);
					probas.add(root.callMethod(GetProbability.getInstance()));
				}
				return probas;
			} finally {
				if (oldEdge == null)
					ctx.delPart("edge");
				else
					ctx.setPart(oldEdge);
			}
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GetProbability.getInstance()),
			};
		}

		@Override
		public Collection<Lock> getLocks(AlgorithmPart root, Void args) {
			// We list locks here recursively, because current implementation cannot express iteration of `AlgorithmPart`s
			Set<Lock> locks = new HashSet<Lock>();
			var ctx = (Context) root;
			var deps = MethodImplementation.getCachedInclusiveTransitiveDependencies(root, ctx.getPart("algorithm"), GetProbability.getInstance(), null);
			var oldEdge = root.callMethod(GetEdge.getInstance());
			try {
				for (var edge: getConsideredEdges(root)) {
					ctx.setPart(edge);
					for (var dep: deps) {
						locks.addAll(dep.getImplementationLocks(ctx));
					}
				}
			} finally {
				if (oldEdge == null)
					ctx.delPart("edge");
				else
					ctx.setPart(oldEdge);
			}
			return locks;
		}

		protected Iterable<Edge> getConsideredEdges(AlgorithmPart root) {
			var ant = root.callMethod(GetAnt.getInstance());
			var storage = ant.getValueStorage();
			var propname = "consideredEdges";
			var lock = storage.getLock(propname).readLock();
			if (!lock.tryLock())
				return null;  // Should not trigger, because we do not expect concurrent processing of a single ant
			try {
				return storage.getValue(propname);
			} finally {
				lock.unlock();
			}
		}
	};
}
