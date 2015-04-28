package de.mt.poltool.gui;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import de.mt.poltool.CsvHandler;
import de.mt.poltool.gui.model.GuiModel;

public class WriteCsvView extends AbstractView {
	private File file;
	private TextField fileName;
	private Button writeButton;

	public WriteCsvView(GuiModel model, Stage primaryStage) {
		super(model, primaryStage);

		fileName = new TextField();
		fileName.setEditable(false);
		fileName.setPrefColumnCount(20);
		fileName.setPromptText("Keine Datei gewählt.");
		fileName.setFont(Font.getDefault());

		Button fileButton = new Button("Wähle");
		fileButton.setOnAction(event -> handleFileChooser());

		writeButton = new Button("Schreiben");
		writeButton.setOnAction(event -> handleWrite());
		writeButton.setDisable(true);

		rootPane.addRow(1, fileButton, fileName);
		rootPane.addRow(2, writeButton);

		this.getChildren().add(rootPane);
	}

	protected void handleFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("CSV Import Datei");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("CSV", "*.csv"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showSaveDialog(primaryStage);
		if (selectedFile != null) {
			String name = selectedFile.toString();
			fileName.setText(name);
			file = selectedFile;
			setStatus("Datei ausgewählt.");
		}
		writeButton.setDisable(selectedFile == null);

	}

	protected void handleWrite() {
		CsvHandler handler = new CsvHandler();
		handler.write(file, model.getSets());
		setStatus("Datei geschrieben.");
	}

	private void setStatus(String status) {
		PolApplication.setStatus(status);
	}

}
