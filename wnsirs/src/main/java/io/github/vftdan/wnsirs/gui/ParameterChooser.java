package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.core.Context;
import javafx.scene.*;

public abstract class ParameterChooser<T> implements Iterable<ParameterChooser.ParameterSetup<T> >, Comparable<ParameterChooser<?> > {
	protected Node uiNode = null;
	protected int priority = 0;

	@Override
	public int compareTo(ParameterChooser<?> other) {
		return priority - other.priority;
	}

	public Node getUiNode() {
		return uiNode;
	}

	public static class ParameterSetup<T> {
		protected T value;

		public ParameterSetup(T value) {
			this.value = value;
		}

		public Context apply(Context ctx) {
			return ctx;
		}

		public boolean shouldSkip(Context ctx) {
			return false;
		}
	}
}
