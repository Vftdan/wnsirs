package io.github.vftdan.wnsirs.methods;

import java.util.*;

import io.github.vftdan.wnsirs.core.*;

public class FilterNeighbors extends HandlerMethodDescriptor<Set<Node>, Void> {
	protected static FilterNeighbors instance = new FilterNeighbors();

	protected FilterNeighbors() {
		super("filterNeighbors", (Class<Set<Node>>)(Class<?>)Set.class, Void.class);
	}

	public static FilterNeighbors getInstance() {
		return instance;
	}

	public static class Emitter extends HandlerMethodDescriptor.Emitter<Set<Node>, Void, Set<Node>, Void> {
		protected static final Emitter instance = new Emitter();

		protected Emitter() {
			super("emitFilterNeighbors", (Class<Set<Node> >)(Class<?>)Set.class, Void.class);
		}

		public static Emitter getInstance() {
			return instance;
		}

		@Override
		public HandlerMethodDescriptor<Set<Node>, Void> getHandlerDescriptor() {
			return FilterNeighbors.getInstance();
		}

		@Override
		public MethodImplementation<Set<Node>, Void> getDefaultImplementation() {
			if (defaultImplementation == null)
				defaultImplementation = new Implementation();
			return defaultImplementation;
		}

		protected Implementation defaultImplementation = null;

		public static class Implementation extends HandlerMethodDescriptor.Emitter.Implementation<Set<Node>, Void, Void, Set<Node>, Void> {
			@Override
			public io.github.vftdan.wnsirs.core.HandlerMethodDescriptor.Emitter<? extends Set<Node>, ? super Void, ? super Set<Node>, ? extends Void> implementationFor() {
				return Emitter.getInstance();
			}
		}
	}
}
