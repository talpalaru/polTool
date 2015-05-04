package de.mt.poltool.gui;

import java.util.Collection;
import java.util.concurrent.Executors;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import de.mt.poltool.LigaImporterTask;
import de.mt.poltool.PolApplication;
import de.mt.poltool.LigaImporterTask.Type;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

public class ImportView extends AbstractView {

	public ImportView(GuiModel model, Stage primaryStage) {
		super(model, primaryStage);

		TextField urlName = new TextField();
		urlName.setEditable(true);
		urlName.setPrefColumnCount(50);
		urlName.setPromptText("Keine URL gewählt.");
		urlName.setFont(Font.getDefault());
		urlName.setText("http://kickern-hamburg.de/liga-tool/mannschaften");

		CheckBox appendCheckbox = new CheckBox("Vorhandene Daten löschen");
		appendCheckbox.setSelected(true);

		ToggleGroup group = new ToggleGroup();
		RadioButton overviewRadio = new RadioButton(
				"von Mannschaftsüberblick importieren.");
		overviewRadio.setToggleGroup(group);
		overviewRadio.setSelected(true);
		RadioButton teamRadio = new RadioButton(
				"von Mannschaftsseite importieren.");
		teamRadio.setToggleGroup(group);
		RadioButton matchRadio = new RadioButton(
				"von Begegnungsseite importieren.");
		matchRadio.setToggleGroup(group);

		Button startButton = new Button("Start");
		startButton.setOnAction(event -> {
			if (appendCheckbox.isSelected())
				model.clear();
			Type type = null;
			if (overviewRadio.isSelected()) {
				type = Type.OVERVIEW;
			} else if (teamRadio.isSelected()) {
				type = Type.TEAM;
			} else if (matchRadio.isSelected()) {
				type = Type.MATCH;
			}
			LigaImporterTask task = new LigaImporterTask();
			task.setUrl(urlName.getText());
			task.setType(type);
			PolApplication.bindProgressBar(task.progressProperty());
			PolApplication.bindStatusText(task.messageProperty());
			task.setOnSucceeded((value) -> {
				model.addMatchSets((Collection<MatchSet>) value.getSource()
						.getValue());
				setStatus("Import Abgeschlossen");
			});
			Executors.newSingleThreadExecutor().execute(task);
		});

		rootPane.addRow(1, urlName);
		rootPane.addRow(2, overviewRadio);
		rootPane.addRow(3, teamRadio);
		rootPane.addRow(4, matchRadio);
		rootPane.addRow(5, appendCheckbox);
		rootPane.addRow(6, startButton);
		this.getChildren().add(rootPane);
	}
}
