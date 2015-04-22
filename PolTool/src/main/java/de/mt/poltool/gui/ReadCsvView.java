package de.mt.poltool.gui;

import java.io.File;
import java.util.Collection;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import de.mt.poltool.CsvImporter;
import de.mt.poltool.gui.model.GuiModel;
import de.mt.poltool.gui.model.MatchSet;

public class ReadCsvView extends Group {
	private GridPane rootPane;
	private GuiModel model;
	private Stage primaryStage;
	private File file;
	private TextField fileName;
	private CheckBox appendCheckbox;

	public ReadCsvView(GuiModel model, Stage primaryStage) {
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
		// rootPane.setGridLinesVisible(true);

		appendCheckbox = new CheckBox("Vorhandene Daten löschen");

		fileName = new TextField();
		fileName.setEditable(false);
		fileName.setPrefColumnCount(20);
		fileName.setPromptText("Keine Datei gewählt.");
		fileName.setFont(Font.getDefault());

		Button fileButton = new Button("Wähle");
		fileButton.setOnAction(event -> handleFileChooser());

		Button readButton = new Button("Einlesen");
		readButton.setOnAction(event -> handleRead());

		rootPane.addRow(1, fileButton, fileName);
		rootPane.addRow(2, appendCheckbox);
		rootPane.addRow(3, readButton);

		this.getChildren().add(rootPane);
	}

	protected void handleFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("CSV Import Datei");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("CSV", "*.csv"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		if (selectedFile != null) {
			String name = selectedFile.toString();
			fileName.setText(name);
			file = selectedFile;
			setStatus("Datei ausgewählt.");
		}
	}

	protected void handleRead() {
		CsvImporter importer = new CsvImporter();
		Collection<MatchSet> matches = importer.read(file);
		if (appendCheckbox.isSelected()) {
			model.clear();
		}
		model.addMatchSets(matches);
		setStatus("Datei eingelesen.");
	}

	private void setStatus(String status) {
		PolApplication.setStatus(status);
	}

}
