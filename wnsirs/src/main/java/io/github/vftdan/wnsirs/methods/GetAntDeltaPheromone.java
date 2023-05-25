package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetAntDeltaPheromone extends GetSpecificValueMethodDescriptor<Double> {
	protected GetAntDeltaPheromone(MethodImplementation<Void, Double> imp) {
		super("getAntDeltaPheromone", Double.class, imp);
	}

	public static GetAntDeltaPheromone getInstance() {
		return instance;
	}

	protected static GetAntDeltaPheromone instance = new GetAntDeltaPheromone(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {
				Dependency.fromDescriptor(GetEdge.getInstance()),
			};
			valueName = "antDeltaPheromone";
		}
	});
}
