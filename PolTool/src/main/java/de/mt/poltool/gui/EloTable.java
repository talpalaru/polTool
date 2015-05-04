package de.mt.poltool.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Stage;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;

import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;
import de.mt.poltool.model.Player;

public class EloTable extends AbstractTable<Player> {

	private static final double eloFaktor = 50d;
	private Map<String, Player> players;

	public EloTable(Stage primaryStage, GuiModel model) {
		super(primaryStage, model);
	}

	public void createTable() {

		getColumns().addAll(
				createColumn("Spieler", "name", String.class, 100d),
				createColumn("ELO", "elo", Integer.class, 40d));

		update();
	}

	public void update() {
		ObservableList<String> playerList = model.getPlayers();
		players = new HashMap<String, Player>();
		for (String name : playerList) {
			players.put(name, new Player(name, 1500));
		}
		ObservableList<MatchSet> sets = new SortedList<MatchSet>(
				model.getSets(), (set1, set2) -> {
					return ComparisonChain.start()
							.compare(set1.getDate(), set2.getDate())
							.compare(set1.getSetNr(), set1.getSetNr())
							.compare(set1.getHomeTeam(), set2.getHomeTeam())
							.result();
				});
		for (MatchSet set : sets) {
			if (isSingle(set)) {
				updateSingleElo(set.getHomePlayer1(), set.getGuestPlayer1(),
						set.getHomeResult(), set.getGuestResult());
			} else {
				updateDoubleElo(set.getHomePlayer1(), set.getHomePlayer2(),
						set.getGuestPlayer1(), set.getGuestPlayer2(),
						set.getHomeResult(), set.getGuestResult());
			}
		}
		// SortedList<Player> sortedSets = new SortedList<Player>(
		// FXCollections.observableArrayList());
		// sortedSets.comparatorProperty().bind(comparatorProperty());
		// setItems(sortedSets);
		getItems().clear();
		for (Player player : players.values()) {
			getItems().add(player);
		}
	}

	private boolean isSingle(MatchSet set) {
		return Strings.isNullOrEmpty(set.getHomePlayer2())
				&& Strings.isNullOrEmpty(set.getGuestPlayer2());
	}

	private void updateSingleElo(String homePlayer1, String guestPlayer1,
			int homeResult, int guestResult) {
		Player home = players.get(homePlayer1);
		Player guest = players.get(guestPlayer1);
		int eloDiff = computeElo(home.getElo(), guest.getElo(), homeResult,
				guestResult);
		home.setElo(home.getElo() + eloDiff);
		guest.setElo(guest.getElo() - eloDiff);
	}

	private void updateDoubleElo(String homePlayer1, String homePlayer2,
			String guestPlayer1, String guestPlayer2, int homeResult,
			int guestResult) {
		Player home1 = players.get(homePlayer1);
		Player guest1 = players.get(guestPlayer1);
		Player home2 = players.get(homePlayer2);
		Player guest2 = players.get(guestPlayer2);
		int eloDiff = computeElo(home1.getElo() + home2.getElo(),
				guest1.getElo() + guest2.getElo(), homeResult, guestResult);
		int halfEloDiff = eloDiff / 2;

		home1.setElo(home1.getElo() + halfEloDiff);
		home2.setElo(home2.getElo() + halfEloDiff);
		guest1.setElo(guest1.getElo() - halfEloDiff);
		guest2.setElo(guest2.getElo() - halfEloDiff);
	}

	private int computeElo(int eloH, int eloG, int homeResult, int guestResult) {
		double h = eloH;
		double g = eloG;

		double quot = (h - g) / 400d;
		double ea = 1d / (1d + Math.pow(10d, quot));
		double hw = (Math.signum(homeResult - guestResult) / 2d) + 0.5d;
		double eloDiff = eloFaktor * (hw - ea);
		return new Double(eloDiff).intValue();
	}

}
