package io.github.vftdan.wnsirs.methods;

import java.util.List;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

public class TransitionAntForCandidates extends MethodDescriptor<List<Node>, Void> {
	protected static TransitionAntForCandidates instance = new TransitionAntForCandidates();

	protected TransitionAntForCandidates() {
		super("transitionAntForCandidates", (Class<List<Node> >)(Class<?>)List.class, Void.class);
	}

	public static TransitionAntForCandidates getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<List<Node>, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<List<Node>, Void> defaultImplementation = new MethodImplementation<List<Node>, Void>() {
		@Override
		public MethodDescriptor<? extends List<Node>, ? super Void> implementationFor() {
			return instance;
		}

		@Override
		public Void call(AlgorithmPart root, List<Node> args) {
			var probas = root.callMethod(CalculateProbabilities.getInstance());
			root.callMethod(NormalizeProbabilities.getInstance(), probas);
			var next = root.callMethod(SelectNextNode.getInstance(), new Pair<List<Node>, List<Double> >(args, probas));
			for (var node: next) {
				root.callMethod(SendAntTo.getInstance(), node);
			}
			return null;
		}

		{
			dependencies = new Dependency[] {
				Dependency.fromDescriptor(CalculateProbabilities.getInstance()),
				Dependency.fromDescriptor(NormalizeProbabilities.getInstance()),
				Dependency.fromDescriptor(SelectNextNode.getInstance()),
			};
		}
	};
}
