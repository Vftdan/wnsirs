package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

public class GetAntBackward extends GetSpecificValueMethodDescriptor<Boolean> {
	protected GetAntBackward(MethodImplementation<Void, Boolean> imp) {
		super("getAntBackward", Boolean.class, imp);
	}

	public static GetAntBackward getInstance() {
		return instance;
	}

	protected static GetAntBackward instance = new GetAntBackward(new AbstractImplementation<Boolean>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "antBackward";
			fallbackValue = false;
		}
	});
}
