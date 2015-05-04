package de.mt.poltool.gui;

import javafx.collections.ListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

public class EloView extends AbstractView {

	public EloView(GuiModel model, Stage primaryStage) {
		super(model, primaryStage);
		EloTable eloTable = new EloTable(primaryStage, model);
		SplitPane split = new SplitPane(new Label("Spielerangliste"), eloTable);
		split.setOrientation(Orientation.VERTICAL);
		model.getSets().addListener(
				(ListChangeListener.Change<? extends MatchSet> c) -> eloTable
						.update());
		this.getChildren().add(split);
	}

}
