package io.github.vftdan.wnsirs.util;

import java.util.Comparator;

public class IdentityHashCodeComparator implements Comparator<Object> {
	@Override
	public int compare(Object lhs, Object rhs) {
		return System.identityHashCode(rhs) - System.identityHashCode(lhs);
	}
}
