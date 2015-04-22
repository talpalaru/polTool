package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import de.mt.poltool.gui.model.MatchSet;

public class TeamGraphModel extends GraphModel {

	public TeamGraphModel(String teamName, Collection<MatchSet> matches,
			LocalDate from, LocalDate to, boolean wheighted) {
		Map<Integer, Double> wins = new TreeMap<Integer, Double>();
		Map<Integer, Double> draws = new TreeMap<Integer, Double>();
		Map<Integer, Double> weighted = new TreeMap<Integer, Double>();
		stageTitle = teamName + " von " + formatDate(from) + " bis "
				+ formatDate(to);
		barTitle = "Gewinnquote in " + matches.size() + " Sätzen";

		double tickSize = 100 / (matches.size() / 16d);
		for (MatchSet set : matches) {
			if (set.getHomeResult() > set.getGuestResult()) {
				increaseBy(draws, set.getSetNr(), tickSize);
				increaseBy(weighted, set.getSetNr(), 0.5d * tickSize);
			} else {
				boolean isHomeTeam = teamName.equals(set.getHomeTeam());
				boolean homeWin = set.getHomeResult() > set.getGuestResult();
				if (isHomeTeam == homeWin) {
					increaseBy(wins, set.getSetNr(), tickSize);
					increaseBy(weighted, set.getSetNr(), tickSize);
				}
			}
		}

		xAxis = new CategoryAxis();
		List<String> categories = new ArrayList<String>();
		for (int i = 1; i < 17; i++) {
			if (wheighted) {
				categories.add(Integer.toString(i) + ". ("
						+ formatPercentage(weighted.get(i)) + ")");
			} else {
				categories.add(Integer.toString(i) + ". ("
						+ formatPercentage(wins.get(i)) + ","
						+ formatPercentage(draws.get(i)) + ")");
			}
		}
		xAxis.setCategories(FXCollections
				.<String> observableArrayList(categories));
		yAxis = new NumberAxis();
		yAxis.setUpperBound(100);
		yAxis.setLowerBound(0);
		yAxis.setAutoRanging(false);
		xAxis.setLabel("Sätze");
		yAxis.setLabel("Prozent");
		xAxis.setTickLabelRotation(70d);
		if (wheighted) {
			XYChart.Series<String, Number> seriesM = createSeries("Gewichtet",
					weighted, true, categories);
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

	private void increaseBy(Map<Integer, Double> values, int setNr,
			double addend) {
		Double value = values.get(setNr);
		if (value == null) {
			value = 0d;
		}
		value = value + addend;
		values.put(setNr, value);
	}

	private XYChart.Series<String, Number> createSeries(String title,
			Map<Integer, Double> values, boolean doColoring,
			List<String> categories) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(title);
		int i = 0;
		for (Entry<Integer, Double> setEntry : values.entrySet()) {
			final XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(
					categories.get(i), setEntry.getValue());
			if (doColoring) {
				data.nodeProperty().addListener(new ChangeListener<Node>() {
					public void changed(ObservableValue<? extends Node> ov,
							Node oldNode, Node newNode) {
						if (newNode != null) {
							if (data.getYValue().intValue() > 75) {
								newNode.setStyle("-fx-bar-fill: crimson;");
							} else if (data.getYValue().intValue() > 60) {
								newNode.setStyle("-fx-bar-fill: red;");
							} else if (data.getYValue().intValue() > 50) {
								newNode.setStyle("-fx-bar-fill: orange;");
							} else if (data.getYValue().intValue() > 40) {
								newNode.setStyle("-fx-bar-fill: green;");
							} else {
								newNode.setStyle("-fx-bar-fill: lightgreen;");
							}
						}
					}
				});
			}
			series.getData().add(data);
			i++;
		}
		return series;
	}
}
