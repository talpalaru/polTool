package de.mt.poltool.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import com.google.common.base.Strings;

import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

public class MatchSetTable extends AbstractTable<MatchSet> {

	public MatchSetTable(GuiModel model) {
		super(model);
	}

	private FilteredList<MatchSet> setList;

	public FilteredList<MatchSet> getSetList() {
		return setList;
	}

	public void setSetList(FilteredList<MatchSet> setList) {
		this.setList = setList;
	}

	protected void createTable() {
		TableColumn<MatchSet, LocalDateTime> date = createColumn("Spieldatum",
				"date", LocalDateTime.class, 100d);
		createColumn("HeimTeam", "homeTeam", String.class, 120d);
		createColumn("Gäste", "guestTeam", String.class, 120d);
		createColumn("Satz", "setNr", Integer.class, 45d);
		createColumn("Heimspieler 1", "homePlayer1", String.class, 140d);
		createColumn("Heimspieler 2", "homePlayer2", String.class, 140d);
		createColumn("Gastspieler 1", "guestPlayer1", String.class, 140d);
		createColumn("Gastspieler 2", "guestPlayer2", String.class, 140d);
		createColumn("Punkte Heim", "homeResult", Integer.class, 70d);
		createColumn("Punkte Gast", "guestResult", Integer.class, 70d);

		date.setCellFactory(column -> {
			return new TableCell<MatchSet, LocalDateTime>() {

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.format(DateTimeFormatter
								.ofPattern("dd.MM.yyyy HH:mm")));
					}
				}

			};
		});

		setList = new FilteredList<MatchSet>(model.getSets(), set -> true);
		SortedList<MatchSet> sortedSets = new SortedList<MatchSet>(setList);
		sortedSets.comparatorProperty().bind(comparatorProperty());
		setItems(sortedSets);
	}

	public void updateFilter(LocalDate fromDate, LocalDate toDate, String team,
			String player, ObservableList<String> leages) {
		getSetList()
				.setPredicate(
						set -> {
							if (fromDate != null
									&& set.getDate().isBefore(
											fromDate.atStartOfDay())) {
								return false;
							}
							if (toDate != null
									&& set.getDate().isAfter(
											toDate.atStartOfDay())) {
								return false;
							}
							if (!Strings.isNullOrEmpty(team)
									&& !set.getTeams().contains(team)) {
								return false;
							}
							if (!Strings.isNullOrEmpty(player)
									&& !set.getPlayers().contains(player)) {
								return false;
							}
							if (leages != null && !leages.isEmpty()
									&& !leages.contains(set.getLeage())) {
								return false;
							}
							return true;
						});
	}

}