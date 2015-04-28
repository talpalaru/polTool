package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mt.poltool.model.MatchSet;

public class PlayerPosGraphModel extends GraphModel {
	public PlayerPosGraphModel(Collection<MatchSet> matches, String playerName,
			LocalDate from, LocalDate to) {

		Map<Integer, Double> appereances = new HashMap<Integer, Double>();

		stageTitle = "Spieler " + playerName + " von " + formatDate(from)
				+ " bis " + formatDate(to);
		barTitle = "Absolute Anzahl der gespielten Sätze (von "
				+ matches.size() + " Sätzen)";

		for (int i = 0; i < 16; i++) {
			appereances.put(i, 0d);
		}

		for (MatchSet set : matches) {
			if (!set.getPlayers().contains(playerName)) {
				continue;
			}
			increaseBy(appereances, set.getSetNr(), 1d);
		}

		List<String> categories = new ArrayList<String>();
		for (int i = 0; i < 16; i++) {
			categories.add(Integer.toString(i + 1) + ". ("
					+ format(appereances.get(i)) + " Sätze)");
		}
		setCategories(categories);

		series.add(createSeries("Gewichtet", appereances, false, categories));

		yAxis.setAutoRanging(true);
		yAxis.setLabel("Anzahl");

	}
}
