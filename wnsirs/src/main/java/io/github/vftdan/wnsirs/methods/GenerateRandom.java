package io.github.vftdan.wnsirs.methods;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;

public class GenerateRandom extends MethodDescriptor<Void, Double> {
	protected static GenerateRandom instance = new GenerateRandom();

	protected GenerateRandom() {
		super("generateRandom", Void.class, Double.class);
	}

	public static GenerateRandom getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Void, Double> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Void, Double> defaultImplementation = new MethodImplementation<Void, Double>() {
		@Override
		public MethodDescriptor<? extends Void, ? super Double> implementationFor() {
			return instance;
		}

		@Override
		public Double call(AlgorithmPart root, Void args) {
			Random rnd;
			if (root instanceof Context) {
				var ctx = (Context) root;
				rnd = ctx.getRandom();
			} else {
				rnd = new Random();
			}
			return rnd.nextDouble();
		}
	};
}
