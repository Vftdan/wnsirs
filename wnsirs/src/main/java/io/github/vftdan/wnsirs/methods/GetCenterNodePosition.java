package io.github.vftdan.wnsirs.methods;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.core.Context.GetScheduler;

import static io.github.vftdan.wnsirs.core.StoredValue.*;
import static io.github.vftdan.wnsirs.core.StoredValue.GetSpecificValueMethodDescriptor.*;

import java.util.ArrayList;

public class GetCenterNodePosition extends GetSpecificValueMethodDescriptor<double[]> {
	protected GetCenterNodePosition(MethodImplementation<Void, double[]> imp) {
		super("getCenterNodePosition", double[].class, imp);
	}

	public static GetCenterNodePosition getInstance() {
		return instance;
	}

	protected static GetCenterNodePosition instance = new GetCenterNodePosition(new AbstractImplementation<double[]>() {
		@Override
		protected WithValueStorage getStoringObject(AlgorithmPart root) {
			return root.callMethod(root, GetNetwork.getInstance(), null);
		}

		@Override
		protected double[] getFallbackValue(AlgorithmPart root) {
			var res = calculate(root);
			if (root instanceof Context)
				root.callMethod(GetScheduler.getInstance()).scheduleTask(0, (Context) root, SetCenterNodePosition.getInstance(), res);
			return res;
		}

		protected double[] calculate(AlgorithmPart root) {
			var network = root.callMethod(GetNetwork.getInstance());
			int count = 0;
			ArrayList<Double> center = new ArrayList<Double>();
			for (var node: network.getNodes()) {
				var pos = node.getPosition();
				while (pos.length < center.size())
					center.add(0.0);
				for (int i = 0; i < pos.length; ++i)
					center.set(i, center.get(i) + pos[i]);
			}
			if (count > 1)
				for (int i = 0; i < center.size(); ++i)
					center.set(i, center.get(i) / count);
			return center.stream().mapToDouble(x -> x).toArray();
		}

		{
			dependencies = new Dependency<?>[] {
				Dependency.fromDescriptor(GetNetwork.getInstance()),
				Dependency.fromDescriptor(GetScheduler.getInstance()),
			};
			valueName = "centerNodePosition";
		}
	});
}
