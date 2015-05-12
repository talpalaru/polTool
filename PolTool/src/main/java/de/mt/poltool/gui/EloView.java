package de.mt.poltool.gui;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import de.mt.poltool.model.GuiModel;

public class EloView extends AbstractView {
	private TeamEloTable teamEloTable;
	private FilteredList<String> leageList;
	private ComboBox<String> leageFilter;
	private NumberField from;
	private NumberField to;
	private PlayerEloTable2 playerEloTable;

	public EloView(GuiModel model, Region parent) {
		super(model, parent);

		teamEloTable = new TeamEloTable(model);
		playerEloTable = new PlayerEloTable2(model);
		TabPane tabPane = new TabPane(new Tab("Spieler", playerEloTable),
				new Tab("Teams", teamEloTable));
		SplitPane split = new SplitPane(createFilterTab(), tabPane);
		split.setOrientation(Orientation.VERTICAL);
		split.prefHeightProperty().bind(parent.heightProperty());
		split.prefWidthProperty().bind(parent.widthProperty());
		split.setPadding(new Insets(10d));
		this.getChildren().add(split);
	}

	private Node createFilterTab() {
		GridPane pane = new GridPane();
		pane.setHgap(10d);
		pane.setVgap(10d);

		leageList = new FilteredList<String>(model.getLeages(), value -> true);
		leageFilter = new ComboBox<String>(leageList);
		// leageFilter.valueProperty().addListener(value -> update());
		from = new NumberField();
		to = new NumberField();
		teamEloTable.fromProperty().bindBidirectional(from.integerProperty());
		teamEloTable.toProperty().bindBidirectional(to.integerProperty());
		pane.add(leageFilter, 1, 1);
		pane.addRow(2, from, to);
		return pane;
	}
}
