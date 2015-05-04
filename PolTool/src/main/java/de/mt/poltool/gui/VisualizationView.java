package de.mt.poltool.gui;

import java.io.File;
import java.util.Collection;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.google.common.base.Strings;

import de.mt.poltool.CsvHandler;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;
import de.mt.poltool.visualisation.GraphView;
import de.mt.poltool.visualisation.model.PlayerGraphModel;
import de.mt.poltool.visualisation.model.PlayerPosGraphModel;
import de.mt.poltool.visualisation.model.TeamGraphModel;
import de.mt.poltool.visualisation.model.TeampPlayerGraphModel;

public class VisualizationView extends AbstractView {
	private DatePicker fromDate;
	private DatePicker toDate;
	private ComboBox<String> teamFilter;
	private ComboBox<String> playerFilter;
	private Text statisics;
	private CheckBox weightedCheckbox;
	private FilteredList<String> playerList;
	private Button showPlayerButton;
	private Button showTeamPlayerButton;
	private Button showTeamButton;
	private Button showPlayerPosButton;
	private MatchSetTable setTable;

	public VisualizationView(GuiModel model, Stage primaryStage) {
		super(model, primaryStage);
		setTable = new MatchSetTable(primaryStage, model);
		SplitPane split = new SplitPane(createFilterTab(), setTable);
		split.setOrientation(Orientation.VERTICAL);
		split.setDividerPositions(0.3d);
		split.prefHeightProperty().bind(
				primaryStage.getScene().heightProperty());
		split.prefWidthProperty().bind(primaryStage.getScene().widthProperty());
		split.setPadding(new Insets(10d));
		this.getChildren().add(split);
	}

	private Node createFilterTab() {
		GridPane pane = new GridPane();
		pane.setHgap(10d);
		pane.setVgap(10d);

		fromDate = new DatePicker();
		fromDate.valueProperty().addListener(value -> update());
		toDate = new DatePicker();
		toDate.valueProperty().addListener(value -> update());

		teamFilter = new ComboBox<String>(model.getTeams());
		teamFilter.valueProperty().addListener(value -> {
			update();
			filterPlayers();
		});
		playerList = new FilteredList<String>(model.getPlayers(), value -> true);
		playerFilter = new ComboBox<String>(playerList);
		playerFilter.valueProperty().addListener(value -> update());

		Button writeButton = new Button("CSV Schreiben");
		writeButton.setOnAction(event -> handleWrite());

		ToolBar fileBar = new ToolBar(writeButton);
		fileBar.setBackground(Background.EMPTY);

		pane.addRow(0, new HBox());
		pane.addRow(1, new Label("Ab:"), fromDate, new Label("Bis: "), toDate);
		pane.addRow(2, new Label("Team"), teamFilter, new Label("Spieler"),
				playerFilter);
		pane.add(createStatsButtonBar(), 0, 3, 4, 1);
		pane.addRow(4, fileBar);

		updateStatisics();
		return pane;
	}

	private void filterPlayers() {
		String team = teamFilter.getValue();
		if (Strings.isNullOrEmpty(team)) {
			playerList.setPredicate(value -> true);
		} else {
			Collection<String> playersByTeam = model.getPlayersByTeam(team);
			playerList.setPredicate(value -> value == null ? false
					: playersByTeam.contains(value));
		}
	}

	private Node createStatsButtonBar() {
		weightedCheckbox = new CheckBox("gewichtet");
		showTeamButton = new Button("Team");
		showTeamButton.setDisable(true);
		showTeamButton.setOnAction(value -> new GraphView(new TeamGraphModel(
				teamFilter.getValue(), setTable.getItems(),
				fromDate.getValue(), toDate.getValue(), weightedCheckbox
						.isSelected()), primaryStage));

		showTeamPlayerButton = new Button("Teamspieler");
		showTeamPlayerButton.setDisable(true);
		showTeamPlayerButton.setOnAction(value -> new GraphView(
				new TeampPlayerGraphModel(setTable.getItems(), teamFilter
						.getValue(), fromDate.getValue(), toDate.getValue(),
						weightedCheckbox.isSelected()), primaryStage));
		statisics = new Text();
		showPlayerButton = new Button("Einzelspieler");
		showPlayerButton.setDisable(true);
		showPlayerButton.setOnAction(value -> new GraphView(
				new PlayerGraphModel(setTable.getItems(), playerFilter
						.getValue(), fromDate.getValue(), toDate.getValue(),
						weightedCheckbox.isSelected()), primaryStage));
		showPlayerPosButton = new Button("Spielerpositionen");
		showPlayerPosButton.setDisable(true);
		showPlayerPosButton.setOnAction(value -> new GraphView(
				new PlayerPosGraphModel(setTable.getItems(), playerFilter
						.getValue(), fromDate.getValue(), toDate.getValue()),
				primaryStage));

		ToolBar bar = new ToolBar(new Label("Statisiken:  "), statisics,
				showTeamButton, showTeamPlayerButton, showPlayerButton,
				showPlayerPosButton, weightedCheckbox);
		bar.setBackground(Background.EMPTY);

		return bar;
	}

	private void update() {
		setTable.updateFilter(fromDate.getValue(), toDate.getValue(),
				teamFilter.getValue(), playerFilter.getValue());

		showTeamButton.setDisable(Strings.isNullOrEmpty(teamFilter.getValue()));
		showTeamPlayerButton.setDisable(Strings.isNullOrEmpty(teamFilter
				.getValue()));
		showPlayerButton.setDisable(Strings.isNullOrEmpty(playerFilter
				.getValue()));
		showPlayerPosButton.setDisable(Strings.isNullOrEmpty(playerFilter
				.getValue()));
		updateStatisics();
	}

	private void updateStatisics() {
		int noSets = setTable.getItems().size();
		int result = 0;
		if (!Strings.isNullOrEmpty(teamFilter.getValue())) {
			String team = teamFilter.getValue();
			for (MatchSet set : setTable.getItems()) {
				int delta = (set.getHomeResult() - set.getGuestResult())
						* (team.equals(set.getHomeTeam()) ? 1 : -1);
				result = result + delta;
			}
		} else if (!Strings.isNullOrEmpty(playerFilter.getValue())) {
			String player = playerFilter.getValue();
			for (MatchSet set : setTable.getItems()) {
				int delta = (set.getHomeResult() - set.getGuestResult())
						* (player.equals(set.getHomePlayer1())
								|| player.equals(set.getHomePlayer2()) ? 1 : -1);
				result = result + delta;
			}

		}
		statisics.setText(noSets + " Sätze, Torverhältnis: " + result + "   ");
	}

	protected void handleWrite() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("CSV Import Datei");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("CSV", "*.csv"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showSaveDialog(primaryStage);
		if (selectedFile != null) {
			CsvHandler handler = new CsvHandler();
			handler.write(selectedFile, setTable.getItems());
			setStatus("Datei " + selectedFile.getName() + " geschrieben.");
		}
	}
}
