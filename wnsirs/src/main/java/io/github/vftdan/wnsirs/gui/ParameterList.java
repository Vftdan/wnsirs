package io.github.vftdan.wnsirs.gui;

import io.github.vftdan.wnsirs.core.Algorithm;
import io.github.vftdan.wnsirs.core.Context;
import io.github.vftdan.wnsirs.gui.ParameterChooser.ParameterSetup;
import java.util.function.Supplier;
import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;

public class ParameterList {
	protected ObservableList<ParameterChooser<?> > choosers = FXCollections.observableArrayList();
	protected ListView<ParameterChooser<?> > listView = new ListView(choosers);

	public Node getUiNode() {
		return listView;
	}

	{
		listView.setCellFactory((view) -> {
			var cell = ParameterChooserCell.create();
			return (ListCell<ParameterChooser<?> >) (ListCell) cell;
		});
		listView.setEditable(true);
		listView.setPrefWidth(320);
	}

	public void addFrom(Map<String, Supplier<ParameterChooser> > factories) {
		var keys = factories.keySet();
		if (keys.isEmpty())
			return;
		var dialog = new ChoiceDialog<String>(keys.iterator().next(), keys);
		dialog.setTitle("Add parameter");
		var selection = dialog.showAndWait();
		if (!selection.isPresent())
			return;
		choosers.add(factories.get(selection.get()).get());
	}

	public Iterable<Context> applyAll(Context context) {
		return applyAll(() -> {
			context.resetClones();
			var newCtx = context.clone();
			var oldAlgo = newCtx.getPart("algorithm");
			var algo = new Algorithm();
			if (oldAlgo != null)
				algo.setPart(oldAlgo);
			newCtx.setPart(algo);
			return newCtx;
		});
	}

	public Iterable<Context> applyAll(Supplier<Context> contexts) {
		var list = new ArrayList(choosers);
		Collections.sort(list);
		return new Iterable<Context>() {
			@Override
			public Iterator<Context> iterator() {
				return new Iterator<Context>() {
					int lastUnfinished = list.size() - 1;
					List<ParameterChooser> choosers = list;
					List<Iterator<ParameterSetup<?> > > iterators = new ArrayList(list.size());
					List<ParameterSetup<?> > values = new ArrayList(list.size());
					Context afterNext = null;
					boolean first = true;

					{
						for (var chooser: choosers) {
							var list = new ArrayList();
							chooser.iterator().forEachRemaining(list::add);
						}
						boolean empty = false;
						for (var chooser: choosers) {
							var it = chooser.iterator();
							iterators.add(it);
							values.add(null);
							if (!iteratorAtNext(values.size() - 1)) {
								empty = true;
								break;
							}
						}
						if (!empty)
							calculateAfterNext();
					}

					@Override
					public boolean hasNext() {
						return afterNext != null;
					}

					@Override
					public Context next() {
						var r = afterNext;
						if (r != null)
							calculateAfterNext();
						return r;
					}

					void calculateAfterNext() {
						while (true) {
							if (!first) {
								if (lastUnfinished < 0) {
									afterNext = null;
									return;
								}
								while (!iterators.get(lastUnfinished).hasNext()) {
									--lastUnfinished;
									if (lastUnfinished < 0) {
										afterNext = null;
										return;
									}
								}
								iteratorAtNext(lastUnfinished);
								for (int i = lastUnfinished + 1; i < choosers.size(); ++i) {
									iterators.set(i, choosers.get(i).iterator());
									if (!iteratorAtNext(i)) {
										afterNext = null;
										return;
									}
								}
								lastUnfinished = choosers.size() - 1;
							}
							first = false;
							var ctx = contexts.get();
							for (var setup: values) {
								if (setup.shouldSkip(ctx))
									continue;
								ctx = setup.apply(ctx);
							}
							afterNext = ctx;
							return;
						}
					}

					boolean iteratorAtNext(int i) {
						var it = iterators.get(i);
						if (!it.hasNext())
							return false;
						values.set(i, it.next());
						return true;
					}
				};
			}
		};
	}

	public static class ParameterChooserCell<T> extends DeletableListCell<ParameterChooser<T> > {
		@Override
		protected Node makeContentNode(ParameterChooser<T> value) {
			if (value == null)
				return null;
			return value.getUiNode();
		}

		public static <T> ParameterChooserCell<T> create() {
			return new ParameterChooserCell<T>();  // Couldn't compile when using constructor directly: Cannot instantiate the type ParameterList.ParameterChooserCell<?>
		}
	}
}
