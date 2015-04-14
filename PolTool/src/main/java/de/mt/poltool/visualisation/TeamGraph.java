package de.mt.poltool.visualisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import de.mt.poltool.model.Match;
import de.mt.poltool.model.PlaySet;

public class TeamGraph extends Application {

	private static String team;
	private static SortedMap<Integer, Double> wins = new TreeMap<Integer, Double>();
	private static SortedMap<Integer, Double> draws = new TreeMap<Integer, Double>();
	private static SortedMap<Integer, Double> losts = new TreeMap<Integer, Double>();
	private static SortedMap<Integer, Double> weighted = new TreeMap<Integer, Double>();
	private static double noOfSets;
	private static int noOfMatches;

	public void showWinsPerSetNr(Collection<Match> matches) {
		team = getTeam(matches);
		noOfMatches = matches.size();
		for (Match match : matches) {
			for (PlaySet set : match.getSets()) {
				noOfSets++;
			}
		}
		noOfSets = noOfSets / 16;
		for (Match match : matches) {
			int isLeft = team.equals(match.getLeftTeam()) ? 1 : -1;
			for (PlaySet set : match.getSets()) {
				int setNr = set.getSetNr();
				if ((set.getLeftResult() - set.getRightResult()) * isLeft > 0) {
					increasePercentage(setNr, wins, (100L / noOfSets));
					increasePercentage(setNr, weighted, (100L / noOfSets));
				} else if (set.getLeftResult() - set.getRightResult() == 0) {
					increasePercentage(setNr, draws, (100L / noOfSets));
					increasePercentage(setNr, weighted, (50L / noOfSets));
				} else {
					increasePercentage(setNr, losts, (100L / noOfSets));
				}
			}
		}
		launch(null);
	}

	private void increasePercentage(int setNr,
			SortedMap<Integer, Double> values, Double amount) {
		Double percentage = values.get(setNr);
		if (percentage == null) {
			percentage = Double.valueOf(0L);
		}
		percentage = percentage + amount;
		values.put(setNr, percentage);
	}

	private String getTeam(Collection<Match> matches) {
		String left = "";
		String right = "";
		for (Match match : matches) {
			if (match.getLeftTeam().equals(left)) {
				return left;
			}
			if (match.getRightTeam().equals(left)) {
				return left;
			}
			if (match.getLeftTeam().equals(right)) {
				return right;
			}
			if (match.getRightTeam().equals(right)) {
				return right;
			}
			left = match.getLeftTeam();
			right = match.getRightTeam();

		}
		return "???";
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle(team);
		final CategoryAxis xAxis = new CategoryAxis();
		List<String> categories = new ArrayList<String>();
		for (int i = 1; i < 17; i++) {
			categories.add(Integer.toString(i));
			categories.add("w" + Integer.toString(i));
		}
		xAxis.setCategories(FXCollections
				.<String> observableArrayList(categories));
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setUpperBound(100);
		yAxis.setLowerBound(0);
		yAxis.setAutoRanging(false);
		final StackedBarChart<String, Number> bc = new StackedBarChart<String, Number>(
				xAxis, yAxis);
		bc.setTitle("Percentages per Set (of " + noOfMatches + " matches)");
		xAxis.setLabel("Set");
		yAxis.setLabel("Percentage");

		XYChart.Series seriesW = createSeries("Wins", wins, false, "");
		XYChart.Series seriesD = createSeries("Draws", draws, false, "");
		XYChart.Series seriesL = createSeries("Losts", losts, false, "");
		XYChart.Series seriesM = createSeries("Weighted", weighted, true, "w");
		Scene scene = new Scene(bc, 800, 600);
		bc.getData().addAll(seriesW, seriesD, seriesL, seriesM);
		// bc.getData().addAll(series3);
		stage.setScene(scene);
		stage.show();
	}

	private XYChart.Series createSeries(String title,
			Map<Integer, Double> values, boolean doColoring, String prefix) {
		XYChart.Series series = new XYChart.Series();
		series.setName(title);
		for (Entry<Integer, Double> setEntry : values.entrySet()) {
			final XYChart.Data<String, Number> data = new XYChart.Data(prefix
					+ setEntry.getKey().toString(), setEntry.getValue());
			if (doColoring) {
				data.nodeProperty().addListener(new ChangeListener<Node>() {
					public void changed(ObservableValue<? extends Node> ov,
							Node oldNode, Node newNode) {
						if (newNode != null) {
							if (data.getYValue().intValue() > 70) {
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
		}
		return series;
	}
}
