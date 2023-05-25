package io.github.vftdan.wnsirs.gui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.*;

public abstract class DeletableListCell<T> extends ListCell<T> {
	private Node content = null;
	protected GridPane layout = new GridPane();
	protected Button deleteButton = new Button("X");
	protected boolean updatingItem = false;

	{
		setEditable(true);
		layout.add(deleteButton, 1, 0);
		GridPane.setHgrow(deleteButton, Priority.NEVER);
		deleteButton.setOnAction((e) -> {
			getListView().getItems().remove(getItem());
		});
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		updatingItem = true;
		setContentNode(makeContentNode(item));
		updatingItem = false;
	}

	protected void setContentNode(Node node) {
		if (content == node)
			return;
		if (content != null) {
			GridPane.clearConstraints(content);
			layout.getChildren().remove(content);
		}
		content = node;
		if (node != null) {
			layout.add(node, 0, 0);
			setGraphic(layout);
		} else {
			setGraphic(null);
		}
	}

	protected abstract Node makeContentNode(T value);
}
