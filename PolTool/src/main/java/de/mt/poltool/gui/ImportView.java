package de.mt.poltool.gui;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import de.mt.poltool.gui.model.GuiModel;

public class ImportView extends Group {

	private GuiModel model;
	private Stage primaryStage;

	public ImportView(GuiModel model, Stage primaryStage) {
		super();
		this.model = model;
		this.primaryStage = primaryStage;
		GridPane rootPane = new GridPane();
		rootPane.prefHeightProperty().bind(
				primaryStage.getScene().heightProperty());
		rootPane.prefWidthProperty().bind(
				primaryStage.getScene().widthProperty());
		rootPane.setHgap(10d);
		rootPane.setVgap(10d);

		TextField urlName = new TextField();
		urlName.setEditable(true);
		urlName.setPrefColumnCount(20);
		urlName.setPromptText("Keine Datei gewählt.");
		urlName.setFont(Font.getDefault());

		// Button fileButton = new Button("Wähle");
		// fileButton.setOnAction(event -> handleFileChooser());
		//
		// Button readButton = new Button("Einlesen");
		// readButton.setOnAction(event -> handleRead());
		//
		// Button showButton = new Button("Zeige");
		// showButton.setOnAction(event -> handleShow());
		//
		// rootPane.addRow(1, fileButton, urlName);
		// rootPane.addRow(2, appendCheckbox);
		// rootPane.addRow(3, readButton, showButton);

		this.getChildren().add(rootPane);
	}

}
