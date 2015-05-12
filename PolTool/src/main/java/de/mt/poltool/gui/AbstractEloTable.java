package de.mt.poltool.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;

import de.mt.poltool.model.EloObject;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

@Deprecated
public abstract class AbstractEloTable extends AbstractTable<EloObject> {

	private Map<String, EloObject> eloMap;
	protected double eloFaktor = 25d;
	protected Collection<String> names;
	private FilteredList<EloObject> eloList;

	public AbstractEloTable(GuiModel model) {
		super(model);
	}

	public AbstractEloTable(ObservableList<EloObject> items) {
		super(items);
	}

	public void update() {
		eloMap = new HashMap<String, EloObject>();

		// initialise elo objects
		for (String name : names) {
			eloMap.put(name, new EloObject(name, 1500));
		}

		// sort sets in the order relevant for elo ranking
		ObservableList<MatchSet> sets = sortMatchSets(model.getSets());

		// calculate elo ranking
		updateElos(sets);

		// set positions for ranked objects
		Collection<EloObject> elos = new TreeSet<EloObject>((p1, p2) -> {
			if (p1 == null) {
				return p2 == null ? 0 : -1;
			}
			if (p2 == null) {
				return 1;
			}
			return ComparisonChain.start().compare(p2.getElo(), p1.getElo())
					.result();
		});
		elos.addAll(eloMap.values());

		// set the table items
		getItems().clear();
		int i = 0;

		for (EloObject eloObject : elos) {
			if (Strings.isNullOrEmpty(eloObject.getName())) {
				continue;
			}
			i++;
			eloObject.setPosition(i);
		}
		eloList = new FilteredList<EloObject>(
				FXCollections.observableArrayList(elos));
		SortedList<EloObject> sortedElos = new SortedList<EloObject>(eloList);
		sortedElos.comparatorProperty().bind(comparatorProperty());
		setItems(sortedElos);
	}

	protected void updateElo(String home1, String guest1, int homeResult,
			int guestResult) {
		EloObject homeELO = eloMap.get(home1);
		EloObject guestELO = eloMap.get(guest1);
		int eloDiff = computeElo(homeELO.getElo(), guestELO.getElo(),
				homeResult, guestResult);
		homeELO.setElo(homeELO.getElo() + eloDiff);
		guestELO.setElo(guestELO.getElo() - eloDiff);
	}

	protected abstract void updateElos(ObservableList<MatchSet> sets);

	protected int computeElo(double elo1, double elo2, int result1, int result2) {
		double quot = (elo1 - elo2) / 400d;
		double ea = 1d / (1d + Math.pow(10d, quot));
		double hw = (Math.signum(result1 - result2) / 2d) + 0.5d;
		double eloDiff = eloFaktor * (hw - ea);
		return new Double(eloDiff).intValue();
	}

	protected ObservableList<MatchSet> sortMatchSets(
			ObservableList<MatchSet> sets) {
		ObservableList<MatchSet> sortedResult = new SortedList<MatchSet>(sets,
				(set1, set2) -> {
					return ComparisonChain.start()
							.compare(set1.getDate(), set2.getDate())
							.compare(set1.getSetNr(), set1.getSetNr())
							.compare(set1.getHomeTeam(), set2.getHomeTeam())
							.result();
				});
		return sortedResult;
	}

	protected EloObject get(String name) {
		return eloMap.get(name);
	}

	public FilteredList<EloObject> getEloList() {
		return eloList;
	}
}