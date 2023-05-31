package io.github.vftdan.wnsirs.watchers;

import io.github.vftdan.wnsirs.core.*;
import static io.github.vftdan.wnsirs.core.StoredValue.*;

public class StoredValueChangeWatcher<T> extends CallMethodWatcher {
	protected String valueName;
	public StoredValueChangeWatcher(SetSpecificValueMethodDescriptor<T> setterDescriptor, String valueName) {
		super(setterDescriptor);
		this.defaultRole = "storedValueChangeWatcher_" + valueName;
		this.valueName = valueName;
	}

	protected CallMethodHandler<T, ?> createHandlerMethod() {
		return new CallMethodHandler("valueChanged_" + valueName, watchedDescriptor.getArgType(), watchedDescriptor.getRetType(), "emitValueChanged_" + valueName);
	}
}
