package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetAntDeltaPheromone extends SetSpecificValueMethodDescriptor<Double> {
	protected SetAntDeltaPheromone(MethodImplementation<Double, Boolean> imp) {
		super("setAntDeltaPheromone", Double.class, imp);
	}

	public static SetAntDeltaPheromone getInstance() {
		return instance;
	}

	protected static SetAntDeltaPheromone instance = new SetAntDeltaPheromone(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetEdge.getInstance())};
			valueName = "antDeltaPheromone";
		}
	});
}
