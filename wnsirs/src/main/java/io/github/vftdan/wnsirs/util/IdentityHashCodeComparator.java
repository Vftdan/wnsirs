package io.github.vftdan.wnsirs.util;

import java.util.Comparator;

public class IdentityHashCodeComparator<T> implements Comparator<T> {
	@Override
	public int compare(T lhs, T rhs) {
		return System.identityHashCode(rhs) - System.identityHashCode(lhs);
	}
}
