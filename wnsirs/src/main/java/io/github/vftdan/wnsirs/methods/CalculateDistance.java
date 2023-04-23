package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import javafx.util.Pair;

public class CalculateDistance extends MethodDescriptor<Pair<double[], double[]>, Double> {
	protected static CalculateDistance instance = new CalculateDistance();

	protected CalculateDistance() {
		super("calculateDistance", (Class<Pair<double[], double[]> >)(Class<?>)Pair.class, Double.class);
	}

	public static CalculateDistance getInstance() {
		return instance;
	}

	@Override
	public MethodImplementation<Pair<double[], double[]>, Double> getDefaultImplementation() {
		return defaultImplementation;
	}

	protected MethodImplementation<Pair<double[], double[]>, Double> defaultImplementation = new MethodImplementation<Pair<double[], double[]>, Double>() {
		@Override
		public MethodDescriptor<? extends Pair<double[], double[]>, ? super Double> implementationFor() {
			return instance;
		}

		@Override
		public Double call(AlgorithmPart root, Pair<double[], double[]> args) {
			double sum = 0;
			var lhs = args.getKey();
			var rhs = args.getValue();
			for (int i = 0; i < lhs.length; ++i) {
				var dif = rhs[i] - lhs[i];
				sum += dif * dif;
			}
			return Math.sqrt(sum);
		}
	};
}
