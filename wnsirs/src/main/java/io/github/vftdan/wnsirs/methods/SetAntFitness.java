package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetAntFitness extends SetSpecificValueMethodDescriptor<Double> {
	protected SetAntFitness(MethodImplementation<Double, Boolean> imp) {
		super("setAntFitness", Double.class, imp);
	}

	public static SetAntFitness getInstance() {
		return instance;
	}

	protected static SetAntFitness instance = new SetAntFitness(new AbstractImplementation<Double>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetEdge.getInstance())};
			valueName = "antFitness";
		}
	});
}
