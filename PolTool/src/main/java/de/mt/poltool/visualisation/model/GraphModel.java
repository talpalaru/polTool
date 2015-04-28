package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public abstract class GraphModel {

	protected String stageTitle;
	protected String barTitle;
	protected CategoryAxis xAxis;
	protected NumberAxis yAxis;
	protected Collection<Series<String, Number>> series = new ArrayList<XYChart.Series<String, Number>>();

	public GraphModel() {
		xAxis = new CategoryAxis();
		xAxis.setLabel("Sätze");
		xAxis.setTickLabelRotation(70d);
		yAxis = new NumberAxis();
		yAxis.setUpperBound(100);
		yAxis.setLowerBound(0);
		yAxis.setAutoRanging(false);
		yAxis.setLabel("Prozent");
	}

	public String getStageTitle() {
		return stageTitle;
	}

	public String getBarTitle() {
		return barTitle;
	}

	public CategoryAxis getXAxis() {
		return xAxis;
	}

	public NumberAxis getYAxis() {
		return yAxis;
	}

	public Collection<Series<String, Number>> getSeries() {
		return series;
	}

	protected String formatDate(LocalDate from) {
		if (from == null) {
			return " - ";
		}
		return from.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	protected String formatPercentage(Double value) {
		return format(value) + "%";
	}

	protected String format(Double value) {
		if (value == null) {
			return "0";
		}
		return Integer.toString(value.intValue());
	}

	protected void increaseBy(Map<Integer, Double> values, int setNr,
			double addend) {
		Double value = values.get(setNr - 1);
		if (value == null) {
			value = 0d;
		}
		value = value + addend;
		values.put(setNr - 1, value);
	}

	protected XYChart.Series<String, Number> createSeries(String title,
			Map<Integer, Double> values, boolean doColoring,
			List<String> categories) {
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(title);
		int i = 0;
		for (Entry<Integer, Double> setEntry : values.entrySet()) {
			Double value = setEntry.getValue();
			if (value < 0.1d) {
				value = 0d;
			}
			final XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(
					categories.get(i), value);
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

	protected void setCategories(List<String> categories) {
		xAxis.setCategories(FXCollections
				.<String> observableArrayList(categories));
	}

}
