package de.mt.poltool.gui;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private FilteredList<MatchSet> setList;
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

	public VisualizationView(GuiModel model, Stage primaryStage) {
		super(model, primaryStage);
		rootPane.addRow(2, createTable());
		rootPane.addRow(1, createFilterTab());
		this.getChildren().add(rootPane);
	}

	private Node createFilterTab() {
		GridPane pane = new GridPane();
		pane.setHgap(10d);
		pane.setVgap(10d);

		fromDate = new DatePicker();
		fromDate.valueProperty().addListener(value -> filterTable());
		toDate = new DatePicker();
		toDate.valueProperty().addListener(value -> filterTable());

		teamFilter = new ComboBox<String>(model.getTeams());
		teamFilter.valueProperty().addListener(value -> {
			filterTable();
			filterPlayers();
		});
		playerList = new FilteredList<String>(model.getPlayers(), value -> true);
		playerFilter = new ComboBox<String>(playerList);
		playerFilter.valueProperty().addListener(value -> filterTable());

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

		createStatisics();
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
				teamFilter.getValue(), setList, fromDate.getValue(), toDate
						.getValue(), weightedCheckbox.isSelected()),
				primaryStage));

		showTeamPlayerButton = new Button("Teamspieler");
		showTeamPlayerButton.setDisable(true);
		showTeamPlayerButton.setOnAction(value -> new GraphView(
				new TeampPlayerGraphModel(setList, teamFilter.getValue(),
						fromDate.getValue(), toDate.getValue(),
						weightedCheckbox.isSelected()), primaryStage));
		statisics = new Text();
		showPlayerButton = new Button("Einzelspieler");
		showPlayerButton.setDisable(true);
		showPlayerButton.setOnAction(value -> new GraphView(
				new PlayerGraphModel(setList, playerFilter.getValue(), fromDate
						.getValue(), toDate.getValue(), weightedCheckbox
						.isSelected()), primaryStage));
		showPlayerPosButton = new Button("Spielerpositionen");
		showPlayerPosButton.setDisable(true);
		showPlayerPosButton.setOnAction(value -> new GraphView(
				new PlayerPosGraphModel(setList, playerFilter.getValue(),
						fromDate.getValue(), toDate.getValue()), primaryStage));

		ToolBar bar = new ToolBar(new Label("Statisiken:  "), statisics,
				showTeamButton, showTeamPlayerButton, showPlayerButton,
				showPlayerPosButton, weightedCheckbox);
		bar.setBackground(Background.EMPTY);

		return bar;
	}

	private TableView<MatchSet> createTable() {

		TableView<MatchSet> table = new TableView<MatchSet>();
		table.prefHeightProperty().bind(
				primaryStage.getScene().heightProperty());
		table.prefWidthProperty().bind(primaryStage.getScene().widthProperty());
		TableColumn<MatchSet, LocalDateTime> date = createColumn("Spieldatum",
				"date", LocalDateTime.class, 100d);
		date.setCellFactory(column -> {
			return new TableCell<MatchSet, LocalDateTime>() {

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.format(DateTimeFormatter
								.ofPattern("dd.MM.yyyy HH:mm")));
					}
				}

			};
		});
		TableColumn<MatchSet, String> homeTeam = createColumn("HeimTeam",
				"homeTeam", String.class, 120d);
		TableColumn<MatchSet, String> guestTeam = createColumn("Gäste",
				"guestTeam", String.class, 120d);
		TableColumn<MatchSet, Integer> setNr = createColumn("Satz", "setNr",
				Integer.class, 45d);
		TableColumn<MatchSet, String> homePlayer1 = createColumn(
				"Heimspieler 1", "homePlayer1", String.class, 140d);
		TableColumn<MatchSet, String> homePlayer2 = createColumn(
				"Heimspieler 2", "homePlayer2", String.class, 140d);
		TableColumn<MatchSet, String> guestPlayer1 = createColumn(
				"Gastspieler 1", "guestPlayer1", String.class, 140d);
		TableColumn<MatchSet, String> guestPlayer2 = createColumn(
				"Gastspieler 2", "guestPlayer2", String.class, 140d);
		TableColumn<MatchSet, Integer> homeResult = createColumn("Punkte Heim",
				"homeResult", Integer.class, 70d);
		TableColumn<MatchSet, Integer> guestResult = createColumn(
				"Punkte Gast", "guestResult", Integer.class, 70d);

		table.getColumns().addAll(date, homeTeam, guestTeam, setNr,
				homePlayer1, homePlayer2, guestPlayer1, guestPlayer2,
				homeResult, guestResult);

		setList = new FilteredList<MatchSet>(model.getSets(), set -> true);
		SortedList<MatchSet> sortedSets = new SortedList<MatchSet>(setList);
		sortedSets.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedSets);
		return table;
	}

	private <T> TableColumn<MatchSet, T> createColumn(String name,
			String propertyName, Class<T> type, double width) {
		TableColumn<MatchSet, T> column = new TableColumn<MatchSet, T>(name);
		column.setCellValueFactory(new PropertyValueFactory<MatchSet, T>(
				propertyName));
		column.setPrefWidth(width);
		return column;
	}

	private void filterTable() {
		setList.setPredicate(set -> {
			if (fromDate.getValue() != null
					&& set.getDate().isBefore(
							fromDate.getValue().atStartOfDay())) {
				return false;
			}
			if (toDate.getValue() != null
					&& set.getDate().isAfter(toDate.getValue().atStartOfDay())) {
				return false;
			}
			if (!Strings.isNullOrEmpty(teamFilter.getValue())
					&& !set.getTeams().contains(teamFilter.getValue())) {
				return false;
			}
			if (!Strings.isNullOrEmpty(playerFilter.getValue())
					&& !set.getPlayers().contains(playerFilter.getValue())) {
				return false;
			}
			return true;
		});

		showTeamButton.setDisable(Strings.isNullOrEmpty(teamFilter.getValue()));
		showTeamPlayerButton.setDisable(Strings.isNullOrEmpty(teamFilter
				.getValue()));
		showPlayerButton.setDisable(Strings.isNullOrEmpty(playerFilter
				.getValue()));
		showPlayerPosButton.setDisable(Strings.isNullOrEmpty(playerFilter
				.getValue()));
		createStatisics();
	}

	private void createStatisics() {
		int noSets = setList.size();
		int result = 0;
		if (!Strings.isNullOrEmpty(teamFilter.getValue())) {
			String team = teamFilter.getValue();
			for (MatchSet set : setList) {
				int delta = (set.getHomeResult() - set.getGuestResult())
						* (team.equals(set.getHomeTeam()) ? 1 : -1);
				result = result + delta;
			}
		} else if (!Strings.isNullOrEmpty(playerFilter.getValue())) {
			String player = playerFilter.getValue();
			for (MatchSet set : setList) {
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
			handler.write(selectedFile, setList);
			PolApplication.setStatus("Datei " + selectedFile.getName()
					+ " geschrieben.");
		}
	}
}
