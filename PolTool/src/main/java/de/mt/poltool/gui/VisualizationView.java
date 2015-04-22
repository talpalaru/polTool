package de.mt.poltool.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.google.common.base.Strings;

import de.mt.poltool.gui.model.GuiModel;
import de.mt.poltool.gui.model.MatchSet;
import de.mt.poltool.visualisation.GraphView;
import de.mt.poltool.visualisation.model.TeamGraphModel;
import de.mt.poltool.visualisation.model.TeampPlayerGraphModel;

public class VisualizationView extends Group {
	private GuiModel model;
	private FilteredList<MatchSet> filteredSets;
	private DatePicker fromDate;
	private DatePicker toDate;
	private ComboBox<String> teamFilter;
	private ComboBox<String> playerFilter;
	private Stage primaryStage;
	private Text statisics;
	private CheckBox weightedCheckbox;

	public VisualizationView(GuiModel model, Stage primaryStage) {
		super();
		this.model = model;
		this.primaryStage = primaryStage;
		Scene scene = primaryStage.getScene();

		BorderPane rootPane = new BorderPane();
		rootPane.prefHeightProperty().bind(scene.heightProperty());
		rootPane.prefWidthProperty().bind(scene.widthProperty());
		rootPane.setCenter(createTable());
		rootPane.setTop(createFilterTab());
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
		teamFilter.valueProperty().addListener(value -> filterTable());
		playerFilter = new ComboBox<String>(model.getPlayers());
		playerFilter.valueProperty().addListener(value -> filterTable());

		pane.addRow(0, new HBox());
		pane.addRow(1, new Label("Ab:"), fromDate, new Label("Bis: "), toDate);
		pane.addRow(2, new Label("Team"), teamFilter, new Label("Spieler"),
				playerFilter);
		pane.add(createStatsButtonBar(), 0, 3, 4, 1);
		// pane.add(createExportButtonBar(), 0, 4, 4, 1);

		createStatisics();
		return pane;
	}

	private Node createStatsButtonBar() {
		weightedCheckbox = new CheckBox("gewichted");
		Button showTeam = new Button("Team1");
		showTeam.setOnAction(value -> new GraphView(new TeamGraphModel(
				teamFilter.getValue(), filteredSets, fromDate.getValue(),
				toDate.getValue(), weightedCheckbox.isSelected()), primaryStage));

		Button showTeamPlayer = new Button("Teamspieler");
		showTeamPlayer.setOnAction(value -> new GraphView(
				new TeampPlayerGraphModel(filteredSets, teamFilter.getValue(),
						fromDate.getValue(), toDate.getValue(),
						weightedCheckbox.isSelected()), primaryStage));
		statisics = new Text();
		Button showPlayer = new Button("Einzelspieler");
		showPlayer.setDisable(true);
		Button showPlayerPos = new Button("Spielerpositionen");
		showPlayerPos.setDisable(true);

		ToolBar bar = new ToolBar(new Label("Statisiken:  "), statisics,
				weightedCheckbox, showTeam, showTeamPlayer, showPlayer,
				showPlayerPos);
		bar.setBackground(Background.EMPTY);

		return bar;
	}

	// private Node createExportButtonBar() {
	// Button exportButton = new Button("Exportiere aktuelle Tabelle");
	// exportButton.setOnAction(value -> new CsvExporter().export(fileName,
	// matches););
	//
	// ToolBar bar = new ToolBar(exportButton);
	// bar.setBackground(Background.EMPTY);
	//
	// return bar;
	// }

	private TableView<MatchSet> createTable() {

		TableView<MatchSet> table = new TableView<MatchSet>();
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

		filteredSets = new FilteredList<MatchSet>(model.getSets(), set -> true);
		SortedList<MatchSet> sortedSets = new SortedList<MatchSet>(filteredSets);
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
		filteredSets
				.setPredicate(set -> {
					if (fromDate.getValue() != null
							&& set.getDate().isBefore(
									fromDate.getValue().atStartOfDay())) {
						return false;
					}
					if (toDate.getValue() != null
							&& set.getDate().isAfter(
									toDate.getValue().atStartOfDay())) {
						return false;
					}
					if (!Strings.isNullOrEmpty(teamFilter.getValue())
							&& !set.getTeams().contains(teamFilter.getValue())) {
						return false;
					}
					if (!Strings.isNullOrEmpty(playerFilter.getValue())
							&& !set.getPlayers().contains(
									playerFilter.getValue())) {
						return false;
					}
					return true;
				});
		createStatisics();
	}

	private void createStatisics() {
		int noSets = filteredSets.size();
		int result = 0;
		if (!Strings.isNullOrEmpty(teamFilter.getValue())) {
			String team = teamFilter.getValue();
			for (MatchSet set : filteredSets) {
				int delta = (set.getHomeResult() - set.getGuestResult())
						* (team.equals(set.getHomeTeam()) ? 1 : -1);
				result = result + delta;
			}
		} else if (!Strings.isNullOrEmpty(playerFilter.getValue())) {
			String player = playerFilter.getValue();
			for (MatchSet set : filteredSets) {
				int delta = (set.getHomeResult() - set.getGuestResult())
						* (player.equals(set.getHomePlayer1())
								|| player.equals(set.getHomePlayer2()) ? 1 : -1);
				result = result + delta;
			}

		}
		statisics.setText(noSets + " Sätze, Torverhältnis: " + result + "   ");

	}
}
