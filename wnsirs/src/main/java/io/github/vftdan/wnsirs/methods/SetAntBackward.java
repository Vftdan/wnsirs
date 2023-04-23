package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.SetSpecificValueMethodDescriptor.*;

public class SetAntBackward extends SetSpecificValueMethodDescriptor<Boolean> {
	protected SetAntBackward(MethodImplementation<Boolean, Boolean> imp) {
		super("setAntBackward", Boolean.class, imp);
	}

	public static SetAntBackward getInstance() {
		return instance;
	}

	protected static SetAntBackward instance = new SetAntBackward(new AbstractImplementation<Boolean>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetAnt.getInstance(), null);
		}

		{
			dependencies = new Dependency<?>[] {Dependency.fromDescriptor(GetAnt.getInstance())};
			valueName = "antBackward";
		}
	});
}
