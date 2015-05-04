/**
 * 
 */
package de.mt.poltool;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.controlsfx.control.StatusBar;
import org.controlsfx.dialog.ExceptionDialog;

import de.mt.poltool.gui.EloView;
import de.mt.poltool.gui.ImportView;
import de.mt.poltool.gui.ReadCsvView;
import de.mt.poltool.gui.VisualizationView;
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

		Tab importTab = createTab("Webimport", new ImportView(model,
				primaryStage));
		Tab readTab = createTab("Read CSV",
				new ReadCsvView(model, primaryStage));
		Tab visTab = createTab("Statistik", new VisualizationView(model,
				primaryStage));

		// GridPane eloView = (GridPane) FXMLLoader.load(getClass().getResource(
		// "gui/EloView.fxml"));
		// Tab eloTab = createTab("Elo", eloView);
		Tab eloTab = createTab("Elo", new EloView(model, primaryStage));

		mainTabPane.getTabs().addAll(importTab, readTab, visTab, eloTab);

		rootPane.setCenter(mainTabPane);
		rootPane.setBottom(statusBar);
		primaryStage.show();
	}

	private Tab createTab(String name, Node content) {
		new Tab();
		Tab tab = new Tab();
		tab.setText(name);
		tab.setClosable(false);
		tab.setContent(content);
		return tab;
	}

	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
			handleException(exception);
		});
		launch(args);
	}

	private static void handleException(Throwable exception) {
		ExceptionDialog ed = new ExceptionDialog(exception);
		ed.show();

	}

	public static void bindProgressBar(ReadOnlyDoubleProperty progressProperty) {
		statusBar.progressProperty().bind(progressProperty);
	}

	public static void bindStatusText(ReadOnlyStringProperty statusProperty) {
		statusBar.textProperty().bind(statusProperty);
	}
}
