package io.github.vftdan.wnsirs.algorithms;

import io.github.vftdan.wnsirs.core.*;

public class StandardHeuristic extends SimpleAlgorithmPart {
	protected String name = "Standard heuristic formula";

	{
		defaultRole = "heuristicFormula";
	}

	@Override
	public String toString() {
		return name;
	}
}
