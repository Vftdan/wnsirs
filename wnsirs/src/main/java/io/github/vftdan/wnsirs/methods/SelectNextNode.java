package io.github.vftdan.wnsirs.methods;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

public class SelectNextNode extends MethodDescriptor<Pair<List<Node>, List<Double> >, Collection<Node> > {
	protected static SelectNextNode instance = new SelectNextNode();

	protected SelectNextNode() {
		super("selectNextNode", (Class<Pair<List<Node>, List<Double> > >)(Class<?>)Pair.class, (Class<Collection<Node> >)(Class<?>)Collection.class);
	}

	public static SelectNextNode getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Pair<List<Node>, List<Double> >, Collection<Node> > getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Pair<List<Node>, List<Double> >, Collection<Node> > defaultImplementation = new MethodImplementation<Pair<List<Node>, List<Double> >, Collection<Node> >() {
		@Override
		public MethodDescriptor<? extends Pair<List<Node>, List<Double> >, ? super Collection<Node> > implementationFor() {
			return instance;
		}

		@Override
		public Collection<Node> call(AlgorithmPart root, Pair<List<Node>, List<Double> > args) {
			double target = root.callMethod(GenerateRandom.getInstance());
			var nodes = args.getKey();
			var probas = args.getValue();
			double accum = 0;
			var length = nodes.size();
			if (length == 0)
				return Collections.emptyList();
			for (int i = 0; i < length; ++i) {
				accum += probas.get(i);
				if (accum >= target)
					return Collections.singleton(nodes.get(i));
			}
			return Collections.singleton(nodes.get(length - 1));
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(GenerateRandom.getInstance()),
			};
		}
	};
}
