package de.mt.poltool.gui;

import java.io.File;
import java.util.Collection;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import de.mt.poltool.CsvHandler;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

public class ReadCsvView extends AbstractView {
	private File file;
	private TextField fileName;
	private CheckBox appendCheckbox;
	private Stage primaryStage;

	public ReadCsvView(GuiModel model, Region parent, Stage primaryStage) {
		super(model, parent);
		this.primaryStage = primaryStage;

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
		CsvHandler handler = new CsvHandler();
		Collection<MatchSet> matches = handler.read(file);
		if (appendCheckbox.isSelected()) {
			model.clear();
		}
		model.addMatchSets(matches);
		setStatus("Datei eingelesen.");
	}

}
