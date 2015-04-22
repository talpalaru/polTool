package de.mt.poltool.visualisation;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.mt.poltool.visualisation.model.GraphModel;

/**
 * @author talpalaru
 *
 */
public class GraphView {
	public GraphView(GraphModel model, Stage primaryStage) {

		Stage stage = new Stage();
		stage.initModality(Modality.NONE);
		stage.initOwner(primaryStage);

		stage.setTitle(model.getStageTitle());
		CategoryAxis xAxis = model.getXAxis();
		NumberAxis yAxis = model.getYAxis();
		StackedBarChart<String, Number> bc = new StackedBarChart<String, Number>(
				xAxis, yAxis);
		bc.setTitle(model.getBarTitle());

		Scene scene = new Scene(bc, 800, 600);
		bc.getData().addAll(model.getSeries());
		stage.setScene(scene);
		stage.show();
	}
}
