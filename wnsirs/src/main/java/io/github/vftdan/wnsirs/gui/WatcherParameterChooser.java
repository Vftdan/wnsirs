package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.watchers.*;
import io.github.vftdan.wnsirs.watchers.WatchersAlgorithmWrapper.BaseWatcher;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static io.github.vftdan.wnsirs.watchers.WatchersAlgorithmWrapper.*;

import java.util.Collections;
import java.util.Iterator;

public class WatcherParameterChooser extends ParameterChooser<BaseWatcher> {
	protected BaseWatcher watcher;
	protected Text title = new Text("Watcher");
	protected GridPane layout = new GridPane();
	protected ParameterSetup setup;

	{
		priority = 10;
		uiNode = layout;
		layout.setHgap(8);
		layout.setVgap(8);
		layout.add(title, 0, 0);
	}

	public WatcherParameterChooser(BaseWatcher watcher) {
		this.watcher = watcher;
		this.setup = new ParameterSetup(watcher);
	}

	public WatcherParameterChooser(String name, BaseWatcher watcher) {
		this(watcher);
		title.setText(name);
	}

	@Override
	public Iterator<ParameterChooser.ParameterSetup<BaseWatcher> > iterator() {
		return Collections.singleton((ParameterChooser.ParameterSetup<BaseWatcher>) setup).iterator();
	}

	public static class ParameterSetup extends ParameterChooser.ParameterSetup<BaseWatcher> {
		public ParameterSetup(BaseWatcher value) {
			super(value);
		}

		@Override
		public Context apply(Context ctx) {
			var algo = ctx.getPart("algorithm");
			if (!(algo instanceof WatchersAlgorithmWrapper)) {
				algo = new WatchersAlgorithmWrapper((CompositeAlgorithmPart) algo);
				ctx.setPart(algo);
			}
			var parent = (WatchersAlgorithmWrapper) algo;
			parent.setWatcher(value);
			ctx.setPart(algo);
			return ctx;
		}
	}
}
