package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetAntFitness extends GetSpecificValueMethodDescriptor<Double> {
	protected GetAntFitness(MethodImplementation<Void, Double> imp) {
		super("getAntFitness", Double.class, imp);
	}

	public static GetAntFitness getInstance() {
		return instance;
	}

	protected static GetAntFitness instance = new GetAntFitness(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		@Override
		protected Double getFallbackValue(AlgorithmPart root) {
			return root.callMethod(GetAntDeltaPheromone.getInstance());
		}

		{
			dependencies = new Dependency<?>[] {
				Dependency.fromDescriptor(GetEdge.getInstance()),
				Dependency.fromDescriptor(GetAntDeltaPheromone.getInstance()),
			};
			valueName = "antFitness";
		}
	});
}
