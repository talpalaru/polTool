package de.mt.poltool.gui;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import de.mt.poltool.model.GuiModel;

public abstract class AbstractTable<S> extends TableView<S> {

	protected GuiModel model;

	public AbstractTable(Stage primaryStage, GuiModel model) {
		this.model = model;
		prefHeightProperty().bind(primaryStage.getScene().heightProperty());
		prefWidthProperty().bind(primaryStage.getScene().widthProperty());
		createTable();
	}

	protected abstract void createTable();

	public AbstractTable(ObservableList<S> items) {
		super(items);
	}

	protected <T> TableColumn<S, T> createColumn(String name,
			String propertyName, Class<T> type, double width) {
		TableColumn<S, T> column = new TableColumn<S, T>(name);
		column.setCellValueFactory(new PropertyValueFactory<S, T>(propertyName));
		column.setPrefWidth(width);
		return column;
	}
}