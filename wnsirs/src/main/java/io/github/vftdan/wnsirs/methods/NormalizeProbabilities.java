package io.github.vftdan.wnsirs.methods;

import java.util.List;

import io.github.vftdan.wnsirs.core.*;

public class NormalizeProbabilities extends MethodDescriptor<List<Double>, Void> {
	protected static NormalizeProbabilities instance = new NormalizeProbabilities();

	protected NormalizeProbabilities() {
		super("normalizeProbabilities", (Class<List<Double> >)(Class<?>)List.class, Void.class);
	}

	public static NormalizeProbabilities getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<List<Double>, Void> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<List<Double>, Void> defaultImplementation = new MethodImplementation<List<Double>, Void>() {
		@Override
		public MethodDescriptor<? extends List<Double>, ? super Void> implementationFor() {
			return instance;
		}

		@Override
		public Void call(AlgorithmPart root, List<Double> args) {
			double sum = 0;
			for (double val: args)
				sum += val;
			if (sum == 0)
				return null;
			for (int i = 0; i < args.size(); ++i)
				args.set(i, args.get(i) / sum);
			return null;
		}
	};
}
