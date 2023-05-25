package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.algorithms.*;

public class DeltaPheromoneParameterChooser extends AlgorithmPartParameterChooser {
	{
		title.setText("Delta pheromone formula");
		register(new StandardDeltaPheromone());
		register(new InverseDistanceSumDeltaPheromone());
	}
}
