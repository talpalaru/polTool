package de.mt.poltool.gui;

import javafx.beans.property.SimpleObjectProperty;
import de.mt.poltool.model.EloObject;
import de.mt.poltool.model.GuiModel;

public class TeamEloTable extends AbstractTable<EloObject> {

	private SimpleObjectProperty<Integer> from = new SimpleObjectProperty<Integer>();
	private SimpleObjectProperty<Integer> to = new SimpleObjectProperty<Integer>();

	public TeamEloTable(GuiModel model) {
		super(model);
	}

	public void createTable() {
		createColumn("Rang", "position", Integer.class, 40d);
		createColumn("Team", "name", String.class, 200d);
		createColumn("ELO", "elo", Integer.class, 100d);
		createColumn("Liga", "leage", String.class, 40d);
		itemsProperty().bind(model.getEloModel().teamEloProperty());

		setPredicate(t -> {
			if (to.isNotNull().get() && t.getElo() > to.get()) {
				return false;
			}
			;

			if (from.isNotNull().get() && t.getElo() < from.get()) {
				return false;
			}
			;
			return true;
		});
	}

	public Integer getTo() {
		return to.get();
	}

	public void setTo(Integer to) {
		this.to.set(to);
	}

	public Integer getFrom() {
		return from.get();
	}

	public void setFrom(Integer from) {
		this.from.set(from);
	}

	public SimpleObjectProperty<Integer> toProperty() {
		return to;
	}

	public SimpleObjectProperty<Integer> fromProperty() {
		return from;
	}
}
