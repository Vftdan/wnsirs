package io.github.vftdan.wnsirs.core;

import java.util.*;
import java.util.concurrent.locks.*;

import javafx.util.Pair;

public class StoredValue<T> {
	protected T value;
	protected ReadWriteLock lock = new ReentrantReadWriteLock();

	public static class GetStoredValueMethodDescriptor<T> extends MethodDescriptor<String, T> {
		protected MethodImplementation<String, T> defaultImplementation;

		protected GetStoredValueMethodDescriptor(Class<T> retType, MethodImplementation<String, T> imp) {
			super("getStoredValue", String.class, retType);
			defaultImplementation = imp;
		}

		@Override
		public MethodImplementation<String, T> getDefaultImplementation() {
			return defaultImplementation;
		}

		public static <T> GetStoredValueMethodDescriptor<T> getInstance() {
			// Cannot check type safety in compile time
			return (GetStoredValueMethodDescriptor<T>) instance;
		}

		protected static GetStoredValueMethodDescriptor<Object> instance = new GetStoredValueMethodDescriptor<Object>(Object.class, new MethodImplementation<String, Object>() {
			@Override
			public MethodDescriptor<String, Object> implementationFor() {
				return instance;
			}

			protected ValueStorage getValueStorage(AlgorithmPart root) {
				if (!(root instanceof WithValueStorage))
					return null;
				return ((WithValueStorage) root).getValueStorage();
			}

			@Override
			public Object call(AlgorithmPart root, String args) {
				ValueStorage storage = getValueStorage(root);
				if (storage == null)
					return null;
				return storage.getValue(args);
			}

			@Override
			public Collection<Lock> getLocks(AlgorithmPart root, String args) {
				ValueStorage storage = getValueStorage(root);
				var lock = storage.getLock(args);
				return Collections.singleton(lock.readLock());
			}
		});
	}

	public static class GetSpecificValueMethodDescriptor<T> extends MethodDescriptor<Void, T> {
		protected MethodImplementation<Void, T> defaultImplementation;

		protected GetSpecificValueMethodDescriptor(String methodName, Class<T> retType, MethodImplementation<Void, T> imp) {
			super(methodName, Void.class, retType);
			defaultImplementation = imp;
		}

		@Override
		public MethodImplementation<Void, T> getDefaultImplementation() {
			return defaultImplementation;
		}

		public abstract static class AbstractImplementation<T> extends MethodImplementation<Void, T> {
			protected GetSpecificValueMethodDescriptor<T> descriptor;
			protected String valueName;

			@Override
			public MethodDescriptor<Void, T> implementationFor() {
				return descriptor;
			}

			protected abstract WithValueStorage getStoringObject(AlgorithmPart root);

			protected ValueStorage getValueStorage(AlgorithmPart root) {
				var storing = getStoringObject(root);
				if (storing == null)
					return null;
				return storing.getValueStorage();
			}

			@Override
			public T call(AlgorithmPart root, Void args) {
				ValueStorage storage = getValueStorage(root);
				if (storage == null)
					return null;
				return storage.getValue(valueName);
			}

			@Override
			public Collection<Lock> getLocks(AlgorithmPart root, Void args) {
				ValueStorage storage = getValueStorage(root);
				var lock = storage.getLock(valueName);
				return Collections.singleton(lock.readLock());
			}
		}
	}

	public static class SetStoredValueMethodDescriptor<T> extends MethodDescriptor<Pair<String, T>, Boolean> {
		protected MethodImplementation<Pair<String, T>, Boolean> defaultImplementation;

		protected SetStoredValueMethodDescriptor(Class<T> retType, MethodImplementation<Pair<String, T>, Boolean> imp) {
			// super("setStoredValue", (Pair<String, T>).class, Boolean.class);
			super("setStoredValue", (Class<Pair<String, T> >)(Class<?>)Pair.class, Boolean.class);
			defaultImplementation = imp;
		}

		@Override
		public MethodImplementation<Pair<String, T>, Boolean> getDefaultImplementation() {
			return defaultImplementation;
		}

		public static <T> SetStoredValueMethodDescriptor<T> getInstance() {
			// Cannot check type safety in compile time
			return (SetStoredValueMethodDescriptor<T>) instance;
		}

		protected static SetStoredValueMethodDescriptor<Object> instance = new SetStoredValueMethodDescriptor<Object>(Object.class, new MethodImplementation<Pair<String, Object>, Boolean>() {
			@Override
			public MethodDescriptor<Pair<String, Object>, Boolean> implementationFor() {
				return instance;
			}

			protected ValueStorage getValueStorage(AlgorithmPart root) {
				if (!(root instanceof WithValueStorage))
					return null;
				return ((WithValueStorage) root).getValueStorage();
			}

			@Override
			public Boolean call(AlgorithmPart root, Pair<String, Object> args) {
				ValueStorage storage = getValueStorage(root);
				if (storage == null)
					return false;
				return storage.setValue(args.getKey(), args.getValue());
			}

			@Override
			public Collection<Lock> getLocks(AlgorithmPart root, Pair<String, Object> args) {
				ValueStorage storage = getValueStorage(root);
				var lock = storage.getLock(args.getKey());
				return Collections.singleton(lock.writeLock());
			}
		});
	}

	public static class SetSpecificValueMethodDescriptor<T> extends MethodDescriptor<T, Boolean> {
		protected MethodImplementation<T, Boolean> defaultImplementation;

		protected SetSpecificValueMethodDescriptor(String methodName, Class<T> retType, MethodImplementation<T, Boolean> imp) {
			super(methodName, retType, Boolean.class);
			defaultImplementation = imp;
		}

		@Override
		public MethodImplementation<T, Boolean> getDefaultImplementation() {
			return defaultImplementation;
		}

		public abstract static class AbstractImplementation<T> extends MethodImplementation<T, Boolean> {
			protected SetSpecificValueMethodDescriptor<T> descriptor;
			protected String valueName;

			@Override
			public MethodDescriptor<T, Boolean> implementationFor() {
				return descriptor;
			}

			protected abstract WithValueStorage getStoringObject(AlgorithmPart root);

			protected ValueStorage getValueStorage(AlgorithmPart root) {
				var storing = getStoringObject(root);
				if (storing == null)
					return null;
				return storing.getValueStorage();
			}

			@Override
			public Boolean call(AlgorithmPart root, T args) {
				ValueStorage storage = getValueStorage(root);
				if (storage == null)
					return null;
				return storage.setValue(valueName, args);
			}

			@Override
			public Collection<Lock> getLocks(AlgorithmPart root, T args) {
				ValueStorage storage = getValueStorage(root);
				var lock = storage.getLock(valueName);
				return Collections.singleton(lock.writeLock());
			}
		}
	}

	public static class ValueStorage {
		protected Map<String, StoredValue> values = new HashMap<String, StoredValue>();

		public <T> StoredValue<T> getStored(String key, boolean create) {
			if (values == null)
				return null;
			if (create)
				return (StoredValue<T>) values.computeIfAbsent(key, k -> createStoredValue(k));
			return (StoredValue<T>) values.get(key);
		}

		public <T> T getValue(String key) {
			StoredValue<T> stored = getStored(key, false);
			if (stored == null)
				return null;
			return stored.value;
		}

		public <T> boolean setValue(String key, T value) {
			StoredValue<T> stored = getStored(key, true);
			if (stored == null)
				return false;
			stored.value = value;
			return true;
		}

		public ReadWriteLock getLock(String key) {
			StoredValue stored = getStored(key, false);
			if (stored == null)
				return null;
			return stored.lock;
		}

		protected <T> StoredValue<T> createStoredValue(String key) {
			return new StoredValue<T>();
		}
	}

	public interface WithValueStorage {
		public ValueStorage getValueStorage();
	}
}
