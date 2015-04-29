package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javafx.scene.chart.XYChart;
import de.mt.poltool.model.MatchSet;

public class TeamGraphModel extends GraphModel {

	public TeamGraphModel(String teamName, Collection<MatchSet> matches,
			LocalDate from, LocalDate to, boolean wheighted) {
		Map<Integer, Double> wins = new TreeMap<Integer, Double>();
		Map<Integer, Double> draws = new TreeMap<Integer, Double>();
		Map<Integer, Double> mixed = new TreeMap<Integer, Double>();
		stageTitle = teamName + " von " + formatDate(from) + " bis "
				+ formatDate(to);
		barTitle = "Gewinnquote in " + matches.size() + " Sätzen";

		for (int i = 0; i < 16; i++) {
			wins.put(i, 0d);
			mixed.put(i, 0d);
			draws.put(i, 0d);
		}

		double tickSize = 100 / (matches.size() / 16d);
		for (MatchSet set : matches) {
			if (set.getHomeResult() == set.getGuestResult()) {
				increaseBy(draws, set.getSetNr(), tickSize);
				increaseBy(mixed, set.getSetNr(), 0.5d * tickSize);
			} else {
				boolean isHomeTeam = teamName.equals(set.getHomeTeam());
				boolean homeWin = set.getHomeResult() > set.getGuestResult();
				if (isHomeTeam == homeWin) {
					increaseBy(wins, set.getSetNr(), tickSize);
					increaseBy(mixed, set.getSetNr(), tickSize);
				}
			}
		}

		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < 16; i++) {
			if (wheighted) {
				categories.add(Integer.toString(i + 1) + ". ("
						+ formatPercentage(mixed.get(i)) + ")");
			} else {
				categories.add(Integer.toString(i + 1) + ". ("
						+ formatPercentage(wins.get(i)) + ","
						+ formatPercentage(draws.get(i)) + ")");
			}
		}
		setCategories(categories);
		if (wheighted) {
			XYChart.Series<String, Number> seriesM = createSeries("Gewichtet",
					mixed, true, categories);
			series.add(seriesM);
		} else {
			XYChart.Series<String, Number> seriesW = createSeries("Gewonnen",
					wins, false, categories);
			XYChart.Series<String, Number> seriesD = createSeries(
					"Unentschieden", draws, false, categories);
			series.add(seriesW);
			series.add(seriesD);
		}
	}
}
