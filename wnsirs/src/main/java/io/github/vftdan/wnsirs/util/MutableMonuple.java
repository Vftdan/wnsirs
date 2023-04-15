package io.github.vftdan.wnsirs.util;

// Mutable closure variable wrapper
public class MutableMonuple<T> {
	public T value;

	public MutableMonuple() {}

	public MutableMonuple(T value) {
		this.value = value;
	}
}
