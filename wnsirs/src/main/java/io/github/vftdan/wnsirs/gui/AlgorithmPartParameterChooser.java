package io.github.vftdan.wnsirs.gui;

import java.util.*;

import io.github.vftdan.wnsirs.algorithms.ForwardBackwardAnt;
import io.github.vftdan.wnsirs.core.*;
import io.github.vftdan.wnsirs.methods.TransitionAnt;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class AlgorithmPartParameterChooser extends ParameterChooser<AlgorithmPart> {
	protected ObservableList<ParameterSetup> potentialValues = FXCollections.observableArrayList();
	protected ObservableList<ParameterSetup> values = FXCollections.observableArrayList();
	protected ListView<ParameterSetup> listView = new ListView(values);
	protected Button addButton = new Button("+");
	protected Text title = new Text("Algorithm part");
	protected GridPane layout = new GridPane();
	protected Set<AlgorithmPartCell> cells = Collections.newSetFromMap(new WeakHashMap<AlgorithmPartCell, Boolean>());

	{
		listView.setPrefHeight(12);
		uiNode = layout;
		layout.add(title, 0, 0);
		layout.add(listView, 0, 1);
		layout.add(addButton, 0, 2);
		addButton.setOnAction((e) -> {
			values.add(potentialValues.iterator().next());
			updateHeight();
		});
		listView.setCellFactory((view) -> {
			var cell = new AlgorithmPartCell(potentialValues);
			cells.add(cell);
			cell.heightProperty().addListener((o) -> {
				updateHeight();
			});
			return cell;
		});
		listView.setEditable(true);
	}

	protected void updateHeight() {
		double height = 12;
		for (var c: cells) {
			height += c.getHeight();
			if (c.getItem() == null)
				break;
		}
		listView.setPrefHeight(height);
	}

	protected void register(ParameterSetup setup) {
		potentialValues.add(setup);
	}

	protected void register(AlgorithmPart algo) {
		register(new ParameterSetup(algo));
	}

	@Override
	public Iterator<ParameterChooser.ParameterSetup<AlgorithmPart> > iterator() {
		List<ParameterChooser.ParameterSetup<AlgorithmPart> > list = new ArrayList();
		for (var setup: values) {
			if (setup == null)
				continue;
			list.add(setup);
		}
		return list.iterator();
	}

	public static class ParameterSetup extends ParameterChooser.ParameterSetup<AlgorithmPart> {
		protected String name;

		public ParameterSetup(String name, AlgorithmPart value) {
			super(value);
			this.name = name;
		}

		public ParameterSetup(AlgorithmPart value) {
			this(value + "", value);
		}

		protected CompositeAlgorithmPart getContainingAlgorithmPart(Context ctx) {
			return (CompositeAlgorithmPart) ctx.getPart("algorithm");
		}

		protected void updateContainingAlgorithmPart(Context ctx) {
			ctx.setPart(getContainingAlgorithmPart(ctx));
		}

		@Override
		public Context apply(Context ctx) {
			var parent = getContainingAlgorithmPart(ctx);
			parent.setPart(value);
			updateContainingAlgorithmPart(ctx);
			return ctx;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class AlgorithmPartCell extends DeletableListCell<ParameterSetup> {
		ComboBox<ParameterSetup> menu;

		public AlgorithmPartCell(ObservableList<ParameterSetup> potentialValues) {
			menu = new ComboBox<ParameterSetup>(potentialValues);
			setContentNode(menu);
			menu.valueProperty().addListener((o) -> {commitEdit(menu.getValue());});
		}

		@Override
		protected Node makeContentNode(ParameterSetup value) {
			if (!updatingItem && menu.getValue() != null)
				return menu;
			if (menu.getValue() != value)
				menu.setValue(value);
			return menu;
		}
	}
}
