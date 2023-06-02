package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetCenterNodePosition extends SetSpecificValueMethodDescriptor<double[]> {
	protected SetCenterNodePosition(MethodImplementation<double[], Boolean> imp) {
		super("setCenterNodePosition", double[].class, imp);
	}

	public static SetCenterNodePosition getInstance() {
		return instance;
	}

	protected static SetCenterNodePosition instance = new SetCenterNodePosition(new AbstractImplementation<double[]>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetNetwork.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {
				Dependency.fromDescriptor(GetNetwork.getInstance()),
			};
			valueName = "centerNodePosition";
		}
	});
}
