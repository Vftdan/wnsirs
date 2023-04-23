package io.github.vftdan.wnsirs.util;

import java.util.Iterator;

public class BareLinkedList<T> implements Iterable<BareLinkedList<T> > {
	public final BareLinkedList<T> next;
	public final T value;
	public final int length;

	public BareLinkedList(T value, BareLinkedList<T> next) {
		this.value = value;
		this.next = next;
		if (next == null) {
			this.length = 1;
		} else {
			this.length = next.length + 1;
		}
	}

	@Override
	public Iterator<BareLinkedList<T> > iterator() {
		return new Iterator<BareLinkedList<T> >() {
			private BareLinkedList<T> node = BareLinkedList.this;

			@Override
			public boolean hasNext() {
				return node != null;
			}

			@Override
			public BareLinkedList<T> next() {
				var n = node;
				this.node = n.next;
				return n;
			}
		};
	}
}
