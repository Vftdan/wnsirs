package io.github.vftdan.wnsirs.core;

import java.util.*;

public class Context extends CompositeAlgorithmPart implements Cloneable {
	public Context() {
		defaultRole = "context";
	}

	public Context clone() {
		var other = new Context();
		other.defaultRole = defaultRole;
		for (var key: possibleImplementationChildren.keySet())
			other.possibleImplementationChildren.put(key, new LinkedList<String>(possibleImplementationChildren.get(key)));
		other.parts.putAll(parts);
		return other;
	}
}
