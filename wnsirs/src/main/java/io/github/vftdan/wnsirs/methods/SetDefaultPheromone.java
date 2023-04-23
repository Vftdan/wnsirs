package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetDefaultPheromone extends SetSpecificValueMethodDescriptor<Double> {
	protected SetDefaultPheromone(MethodImplementation<Double, Boolean> imp) {
		super("setDefaultPheromone", Double.class, imp);
	}

	public static SetDefaultPheromone getInstance() {
		return instance;
	}

	protected static SetDefaultPheromone instance = new SetDefaultPheromone(new AbstractImplementation<Double>() {
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
