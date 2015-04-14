package de.mt.poltool.visualisation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import de.mt.poltool.model.Match;
import de.mt.poltool.model.PlaySet;

public class PlayerGraph extends Application {

	private static String team;

	private static Map<String, Double> playerWins = new TreeMap<String, Double>();
	private static Map<String, Double> playerDraws = new TreeMap<String, Double>();
	private static Map<String, Double> playerAppearance = new TreeMap<String, Double>();
	private static double noOfSets;
	private static int noOfMatches;

	public void showWinsPerPlayer(Collection<Match> matches) {
		team = getTeam(matches);
		noOfMatches = matches.size();
		for (Match match : matches) {
			boolean isLeft = team.equals(match.getLeftTeam());
			for (PlaySet set : match.getSets()) {
				noOfSets++;
				if (isLeft) {
					increasePlayerAppereance(set.getLeftPlayer1());
					increasePlayerAppereance(set.getLeftPlayer2());
				} else {
					increasePlayerAppereance(set.getRightPlayer1());
					increasePlayerAppereance(set.getRightPlayer2());
				}
			}
		}

		noOfSets = noOfSets / 16;
		for (Match match : matches) {
			int isLeft = team.equals(match.getLeftTeam()) ? 1 : -1;
			for (PlaySet set : match.getSets()) {
				int setNr = set.getSetNr();
				if ((set.getLeftResult() - set.getRightResult()) * isLeft > 0) {
					if (isLeft > 0) {
						increasePlayerPercentage(set.getLeftPlayer1(),
								playerWins);
						increasePlayerPercentage(set.getLeftPlayer2(),
								playerWins);
					}
				} else if (set.getLeftResult() - set.getRightResult() == 0) {
					if (isLeft > 0) {
						increasePlayerPercentage(set.getLeftPlayer1(),
								playerDraws);
						increasePlayerPercentage(set.getLeftPlayer2(),
								playerDraws);
					} else {
						increasePlayerPercentage(set.getRightPlayer1(),
								playerDraws);
						increasePlayerPercentage(set.getRightPlayer2(),
								playerDraws);
					}
				} else {
					if (isLeft < 0) {
						increasePlayerPercentage(set.getRightPlayer1(),
								playerDraws);
						increasePlayerPercentage(set.getRightPlayer2(),
								playerDraws);
					}
				}
			}
		}
		launch(null);
	}

	private void increasePlayerPercentage(String player,
			Map<String, Double> stats) {
		if (player != null) {
			Double percentage = stats.get(player);
			if (percentage == null) {
				percentage = 0d;
			}
			percentage = percentage + (100 / playerAppearance.get(player));
			stats.put(player, percentage);
		}
	}

	private void increasePlayerAppereance(String player) {
		if (player != null && !player.isEmpty()) {
			Double count = playerAppearance.get(player);
			if (count == null) {
				count = 0d;
			}
			count++;
			playerAppearance.put(player, count);
		}

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
		xAxis.setCategories(FXCollections
				.<String> observableArrayList(playerAppearance.keySet()));
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setUpperBound(100);
		yAxis.setLowerBound(0);
		yAxis.setAutoRanging(false);
		final StackedBarChart<String, Number> bc = new StackedBarChart<String, Number>(
				xAxis, yAxis);
		bc.setTitle("Percentages per Set (of " + noOfMatches + " matches)");
		xAxis.setLabel("Set");
		yAxis.setLabel("Percentage");

		XYChart.Series seriesW = createSeries("Wins", playerWins, "");
		XYChart.Series seriesD = createSeries("Draws", playerDraws, "");
		// XYChart.Series seriesL = createSeries("Losts", losts, false, "");
		// XYChart.Series seriesM = createSeries("Weighted", weighted, true,
		// "w");
		Scene scene = new Scene(bc, 800, 600);
		bc.getData().addAll(seriesW, seriesD);// , seriesL, seriesM);
		// bc.getData().addAll(series3);
		stage.setScene(scene);
		stage.show();
	}

	private XYChart.Series createSeries(String title,
			Map<String, Double> values, String prefix) {
		XYChart.Series series = new XYChart.Series();
		series.setName(title);
		for (Entry<String, Double> setEntry : values.entrySet()) {
			String player = setEntry.getKey().toString();
			final XYChart.Data<String, Number> data = new XYChart.Data(prefix
					+ player, setEntry.getValue());
			series.getData().add(data);
		}
		return series;
	}
}
