package de.mt.poltool.gui;

import de.mt.poltool.model.EloObject;
import de.mt.poltool.model.GuiModel;

public class PlayerEloTable2 extends AbstractTable<EloObject> {

	public PlayerEloTable2(GuiModel model) {
		super(model);
	}

	@Override
	protected void createTable() {
		createColumn("Rang", "position", Integer.class, 40d);
		createColumn("Spieler", "name", String.class, 200d);
		createColumn("ELO", "elo", Integer.class, 100d);
		itemsProperty().bind(model.getEloModel().playerEloProperty());
	}

}
