package de.mt.poltool.gui;

import java.util.Collection;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import de.mt.poltool.PolApplication;
import de.mt.poltool.model.GuiModel;

public class AbstractView extends Group {

	protected GuiModel model;
	protected Stage primaryStage;
	protected GridPane rootPane;
	protected StringProperty statusProperty;

	public AbstractView(GuiModel model, Stage primaryStage) {
		super();
		this.model = model;
		this.primaryStage = primaryStage;
		rootPane = new GridPane();
		rootPane.prefHeightProperty().bind(
				primaryStage.getScene().heightProperty());
		rootPane.prefWidthProperty().bind(
				primaryStage.getScene().widthProperty());
		rootPane.setHgap(10d);
		rootPane.setVgap(10d);
		rootPane.setPadding(new Insets(10d));
		statusProperty = new SimpleStringProperty();
		PolApplication.bindStatusText(statusProperty);
	}

	public AbstractView(Node... children) {
		super(children);
	}

	public AbstractView(Collection<Node> children) {
		super(children);
	}

	protected void setStatus(String status) {
		statusProperty.set(status);
	}

}