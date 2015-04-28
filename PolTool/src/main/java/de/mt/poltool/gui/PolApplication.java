/**
 * 
 */
package de.mt.poltool.gui;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.controlsfx.control.StatusBar;

import de.mt.poltool.model.GuiModel;

/**
 * @author talpalaru
 *
 */
public class PolApplication extends Application {

	private GuiModel model;
	private static StatusBar statusBar = new StatusBar();

	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new GuiModel();
		BorderPane rootPane = new BorderPane();
		TabPane mainTabPane = new TabPane();
		Scene scene = new Scene(rootPane, 620, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Pol Tool");
		rootPane.prefHeightProperty().bind(scene.heightProperty());
		rootPane.prefWidthProperty().bind(scene.widthProperty());

		Tab importTab = new Tab();
		importTab.setText("Import Web");
		importTab.setClosable(false);
		importTab.setContent(new ImportView(model, primaryStage));

		Tab readTab = new Tab();
		readTab.setText("Read CSV");
		readTab.setClosable(false);
		readTab.setContent(new ReadCsvView(model, primaryStage));

		Tab visTab = new Tab();
		visTab.setText("Visualization");
		visTab.setClosable(false);
		visTab.setContent(new VisualizationView(model, primaryStage));

		mainTabPane.getTabs().addAll(importTab, readTab, visTab);

		rootPane.setCenter(mainTabPane);
		rootPane.setBottom(statusBar);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
			handleException(exception);
		});
		launch(args);
	}

	private static void handleException(Throwable exception) {
		setStatus(exception.getMessage());
	}

	public static void setStatus(String status) {
		// statusBar.setText(status);
	}

	public static void bindProgressBar(ReadOnlyDoubleProperty progressProperty) {
		statusBar.progressProperty().bind(progressProperty);
	}

	public static void bindStatusText(ReadOnlyStringProperty statusProperty) {
		statusBar.textProperty().bind(statusProperty);
	}
}
