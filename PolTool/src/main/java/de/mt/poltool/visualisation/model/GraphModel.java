package de.mt.poltool.visualisation.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

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
		if (value == null) {
			return "0%";
		}
		return value.intValue() + "%";
	}

}
