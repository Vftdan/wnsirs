package io.github.vftdan.wnsirs.core;

import java.util.*;

public class SimulationObject extends SimpleAlgorithmPart implements StoredValue.WithValueStorage {
	protected StoredValue.ValueStorage storedValues = new StoredValue.ValueStorage();

	public StoredValue.ValueStorage getValueStorage() {
		return storedValues;
	}

	{
		registerImplementation(StoredValue.GetStoredValueMethodDescriptor.getInstance(), null);
		registerImplementation(StoredValue.SetStoredValueMethodDescriptor.getInstance(), null);
	}
}
