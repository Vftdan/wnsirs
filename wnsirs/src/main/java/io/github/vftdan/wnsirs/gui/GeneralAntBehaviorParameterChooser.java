package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.algorithms.*;

public class GeneralAntBehaviorParameterChooser extends AlgorithmPartParameterChooser {
	{
		title.setText("General ant behavior");
		register(new ForwardBackwardAnt());
	}
}
