package io.github.vftdan.wnsirs.algorithms;

import io.github.vftdan.wnsirs.core.*;

public class StandardDeltaPheromone extends SimpleAlgorithmPart {
	protected String name = "Standard delta pheromone formula";

	{
		defaultRole = "deltaPheromoneFormula";
	}

	@Override
	public String toString() {
		return name;
	}
}
