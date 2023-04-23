package io.github.vftdan.wnsirs.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class CompositeLock implements Lock {
	protected static final Comparator<? super Lock> comparator = new IdentityHashCodeComparator();
	protected SortedSet<Lock> locks;
	protected Stack<Lock> locked;

	public CompositeLock(Collection<Lock> locks) {
		this.locks = new TreeSet<Lock>(comparator);
		this.locks.addAll(locks);
		locked = new Stack<Lock>();
	}

	protected synchronized void releaseLocks() {
		while (!locked.empty()) {
			var l = locked.peek();
			l.unlock();
			locked.pop();
		}
	}

	@Override
	public synchronized void lock() {
		try {
			for (var l: locks) {
				l.lock();
				locked.push(l);
			}
		} finally {
			releaseLocks();
		}
	}

	@Override
	public synchronized void lockInterruptibly() throws InterruptedException {
		try {
			for (var l: locks) {
				l.lockInterruptibly();
				locked.push(l);
			}
		} finally {
			releaseLocks();
		}
	}

	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException("Conditions are not supported by base CompositeLock");
	}

	@Override
	public synchronized boolean tryLock() {
		try {
			for (var l: locks) {
				if (!l.tryLock()) {
					releaseLocks();
					return false;
				}
				locked.push(l);
			}
		} finally {
			releaseLocks();
		}
		return true;
	}

	@Override
	public synchronized boolean tryLock(long timeout, TimeUnit timeoutUnit) throws InterruptedException {
		try {
			for (var l: locks) {
				// TODO subtract elapsed time
				if (!l.tryLock(timeout, timeoutUnit)) {
					releaseLocks();
					return false;
				}
				locked.push(l);
			}
		} finally {
			releaseLocks();
		}
		return true;
	}

	@Override
	public synchronized void unlock() {
		releaseLocks();
	}
}
