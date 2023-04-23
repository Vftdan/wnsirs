package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetDefaultPheromone extends GetSpecificValueMethodDescriptor<Double> {
	protected GetDefaultPheromone(MethodImplementation<Void, Double> imp) {
		super("getDefaultPheromone", Double.class, imp);
	}

	public static GetDefaultPheromone getInstance() {
		return instance;
	}

	protected static GetDefaultPheromone instance = new GetDefaultPheromone(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetNetwork.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetEdge.getInstance())};
			valueName = "defaultPheromone";
		}
	});
}
