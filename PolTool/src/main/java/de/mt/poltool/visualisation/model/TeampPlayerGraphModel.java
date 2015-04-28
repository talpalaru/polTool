package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import com.google.common.base.Strings;

import de.mt.poltool.gui.model.MatchSet;

public class TeampPlayerGraphModel extends GraphModel {

	private Map<String, Double> playerWins = new TreeMap<String, Double>();
	private Map<String, Double> playerDraws = new TreeMap<String, Double>();
	private Map<String, Double> playerMixed = new TreeMap<String, Double>();
	private Map<String, Double> playerAppearance = new TreeMap<String, Double>();

	public TeampPlayerGraphModel(Collection<MatchSet> matches, String team,
			LocalDate from, LocalDate to, boolean weighted) {

		stageTitle = "Spieler der Mannschaft " + team + " von " + formatDate(from)
				+ " bis " + formatDate(to);
		barTitle = "Gewinnquote in Prozent (von " + matches.size() + " Sätzen)";

		for (MatchSet set : matches) {
			boolean isHome = team.equals(set.getHomeTeam());
			if (isHome) {
				increasePlayerAppereance(set.getHomePlayer1());
				increasePlayerAppereance(set.getHomePlayer2());
			} else {
				increasePlayerAppereance(set.getGuestPlayer1());
				increasePlayerAppereance(set.getGuestPlayer2());
			}
		}

		for (MatchSet set : matches) {
			boolean isHome = team.equals(set.getHomeTeam());
			if ((set.getHomeResult() - set.getGuestResult()) > 0) {
				if (isHome) {
					increasePlayerPercentage(set.getHomePlayer1(), true);
					increasePlayerPercentage(set.getHomePlayer2(), true);
				}
			} else if (set.getHomeResult() - set.getGuestResult() == 0) {
				if (isHome) {
					increasePlayerPercentage(set.getHomePlayer1(), false);
					increasePlayerPercentage(set.getHomePlayer2(), false);
				} else {
					increasePlayerPercentage(set.getGuestPlayer1(), false);
					increasePlayerPercentage(set.getGuestPlayer2(), false);
				}
			} else {
				if (!isHome) {
					increasePlayerPercentage(set.getGuestPlayer1(), true);
					increasePlayerPercentage(set.getGuestPlayer2(), true);
				}
			}
		}

		List<String> categories = new ArrayList<String>();
		for (String name : playerAppearance.keySet()) {
			categories.add(createCategoryName(name));
		}
		setCategories(categories);
		xAxis.setLabel("Spieler");

		if (weighted) {
			Series<String, Number> seriesM = createSeries("Gewichtet",
					playerMixed);
			series.add(seriesM);
		} else {
			XYChart.Series<String, Number> seriesW = createSeries("Gewonnen",
					playerWins);
			XYChart.Series<String, Number> seriesD = createSeries(
					"Unentschieden", playerDraws);

			series.add(seriesW);
			series.add(seriesD);
		}
	}

	private String createCategoryName(String name) {
		return name
				+ "\n("
				+ formatPercentage(playerWins.get(name)
						/ playerAppearance.get(name) * 100d)
				+ ", "
				+ formatPercentage(playerDraws.get(name).intValue()
						/ playerAppearance.get(name) * 100d) + ")"
				+ "\n in " + format(playerAppearance.get(name)) + " Sätzen";
	}

	private void increasePlayerPercentage(String player, boolean isWin) {
		if (!Strings.isNullOrEmpty(player)) {
			if (isWin) {
				Double wins = playerWins.get(player);
				wins = wins + 1;
				playerWins.put(player, wins);
				Double mixed = playerMixed.get(player);
				mixed = mixed + 1;
				playerMixed.put(player, mixed);
			} else {
				Double draws = playerDraws.get(player);
				draws = draws + 1;
				playerDraws.put(player, draws);
				Double mixed = playerMixed.get(player);
				mixed = mixed + 0.5;
				playerMixed.put(player, mixed);
			}
		}
	}

	private void increasePlayerAppereance(String player) {
		if (!Strings.isNullOrEmpty(player)) {
			Double count = playerAppearance.get(player);
			if (count == null) {
				count = 0d;
			}
			count++;
			playerAppearance.put(player, count);
			playerWins.put(player, 0d);
			playerDraws.put(player, 0d);
			playerMixed.put(player, 0d);
		}

	}

	private XYChart.Series<String, Number> createSeries(String title,
			Map<String, Double> values) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(title);
		for (Entry<String, Double> setEntry : values.entrySet()) {
			String player = setEntry.getKey().toString();
			final XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(
					createCategoryName(player), setEntry.getValue()
							/ playerAppearance.get(player) * 100d);
			series.getData().add(data);
		}
		return series;
	}
}
