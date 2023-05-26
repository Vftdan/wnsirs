package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.algorithms.*;

public class HeuristicParameterChooser extends AlgorithmPartParameterChooser {
	{
		title.setText("Heuristic formula");
		register(new StandardHeuristic());
		register(new TargetDistanceAwareHeuristic());
	}
}
