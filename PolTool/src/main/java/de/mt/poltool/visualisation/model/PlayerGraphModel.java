package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mt.poltool.model.MatchSet;

public class PlayerGraphModel extends GraphModel {
	public PlayerGraphModel(Collection<MatchSet> matches, String playerName,
			LocalDate from, LocalDate to, boolean weighted) {

		Map<Integer, Double> appereances = new HashMap<Integer, Double>();
		Map<Integer, Double> wins = new HashMap<Integer, Double>();
		Map<Integer, Double> draws = new HashMap<Integer, Double>();
		Map<Integer, Double> mixed = new HashMap<Integer, Double>();

		stageTitle = "Spieler " + playerName + " von " + formatDate(from)
				+ " bis " + formatDate(to);
		barTitle = "Gewinnqoute in Prozent (von " + matches.size() + " Sätzen)";

		for (int i = 0; i < 16; i++) {
			wins.put(i, 0d);
			mixed.put(i, 0d);
			draws.put(i, 0d);
			appereances.put(i, 0d);
		}

		for (MatchSet set : matches) {
			if (!set.getPlayers().contains(playerName)) {
				continue;
			}
			increaseBy(appereances, set.getSetNr(), 1d);
			if (set.getHomeResult() == set.getGuestResult()) {
				increaseBy(draws, set.getSetNr(), 1d);
				increaseBy(mixed, set.getSetNr(), 0.5d);
			} else {
				boolean ishHome = playerName.equals(set.getHomePlayer1())
						|| playerName.equals(set.getHomePlayer2());
				boolean homeWin = set.getHomeResult() == set.getGuestResult();
				if (ishHome == homeWin) {
					increaseBy(wins, set.getSetNr(), 1d);
					increaseBy(mixed, set.getSetNr(), 1d);
				}
			}
		}

		for (int i = 0; i < 16; i++) {
			Double a = appereances.get(i);
			Double w = wins.get(i) * 100d / a;
			Double d = draws.get(i) * 100d / a;
			Double m = mixed.get(i) * 100d / a;
			if (a < 1d) {
				wins.put(i, 0d);
				mixed.put(i, 0d);
				draws.put(i, 0d);

			} else {
				wins.put(i, w);
				mixed.put(i, m);
				draws.put(i, d);
			}
		}

		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < 16; i++) {
			categories.add(Integer.toString(i + 1) + ". ("
					+ formatPercentage(wins.get(i)) + ", "
					+ formatPercentage(draws.get(i)) + ") "
					+ format(appereances.get(i)) + "\nSätze");
		}
		setCategories(categories);

		if (weighted) {
			series.add(createSeries("Gewichtet", mixed, true, categories));
		} else {
			series.add(createSeries("Gewonnen", wins, false, categories));
			series.add(createSeries("Unentschieden", draws, false, categories));
		}
	}
}
