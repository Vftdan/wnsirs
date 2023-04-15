package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetPheromone extends SetSpecificValueMethodDescriptor<Double> {
	protected SetPheromone(MethodImplementation<Double, Boolean> imp) {
		super("setPheromone", Double.class, imp);
	}

	public static SetPheromone getInstance() {
		return instance;
	}

	protected static SetPheromone instance = new SetPheromone(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetEdge.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetEdge.getInstance())};
			valueName = "pheromone";
		}
	});
}
