package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetPheromone extends GetSpecificValueMethodDescriptor<Double> {
	protected GetPheromone(MethodImplementation<Void, Double> imp) {
		super("getPheromone", Double.class, imp);
	}

	public static GetPheromone getInstance() {
		return instance;
	}

	protected static GetPheromone instance = new GetPheromone(new AbstractImplementation<Double>() {
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
