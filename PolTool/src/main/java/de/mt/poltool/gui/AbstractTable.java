package de.mt.poltool.gui;

import java.util.function.Predicate;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import de.mt.poltool.model.GuiModel;

public abstract class AbstractTable<S> extends TableView<S> {

	protected GuiModel model;
	private ObjectProperty<Predicate<? super S>> predicate;

	public AbstractTable(GuiModel model) {
		this.model = model;
		// prefHeightProperty().bind(parent.heightProperty());
		// prefWidthProperty().bind(parent.widthProperty());
		FilteredList<S> filteredList = new FilteredList<S>(
				FXCollections.observableArrayList());
		predicate = filteredList.predicateProperty();
		setItems(filteredList);

		createTable();
	}

	protected abstract void createTable();

	@Deprecated
	public AbstractTable(ObservableList<S> items) {
		super(items);
	}

	protected <T> TableColumn<S, T> createColumn(String name,
			String propertyName, Class<T> type, double width) {
		TableColumn<S, T> column = new TableColumn<S, T>(name);
		column.setCellValueFactory(new PropertyValueFactory<S, T>(propertyName));
		column.setPrefWidth(width);
		getColumns().add(column);
		return column;
	}

	public void setPredicate(Predicate<S> predicate) {
		this.predicate.set(predicate);
	}
}