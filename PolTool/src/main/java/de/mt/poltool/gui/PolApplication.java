/**
 * 
 */
package de.mt.poltool.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import de.mt.poltool.model.GuiModel;

/**
 * @author talpalaru
 *
 */
public class PolApplication extends Application {

	private GuiModel model;
	private static Text statusBar = new Text("Gestartet");

	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new GuiModel();
		Group root = new Group();
		TabPane mainTabPane = new TabPane();
		Scene scene = new Scene(root, 620, 400);
		primaryStage.setTitle("Pol Tool");
		primaryStage.setScene(scene);
		BorderPane rootPane = new BorderPane();
		rootPane.prefHeightProperty().bind(scene.heightProperty());
		rootPane.prefWidthProperty().bind(scene.widthProperty());
		root.getChildren().add(rootPane);

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

		statusBar.setFont(Font.font(Font.getDefault().getFamily(),
				FontWeight.EXTRA_BOLD, Font.getDefault().getSize()));
		statusBar.setFill(Color.BROWN);
		statusBar.setStyle("fx-background-color: #1d1d1d");

		rootPane.setCenter(mainTabPane);
		rootPane.setTop(statusBar);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void setStatus(String status) {
		statusBar.setText(status);
	}
}
