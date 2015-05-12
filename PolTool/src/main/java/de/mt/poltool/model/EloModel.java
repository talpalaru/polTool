package de.mt.poltool.model;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class EloModel {

	private static final double eloFaktor = 20d;
	private SimpleListProperty<EloObject> teamElo;
	private SimpleListProperty<EloObject> playerElo;
	private Map<String, EloObject> playerMap;
	private Map<String, EloObject> teamMap;
	private GuiModel model;

	public EloModel(GuiModel model) {
		this.model = model;
		teamMap = new TreeMap<String, EloObject>();
		playerMap = new TreeMap<String, EloObject>();
		teamElo = new SimpleListProperty<EloObject>(
				FXCollections.observableArrayList());
		playerElo = new SimpleListProperty<EloObject>(
				FXCollections.observableArrayList());
	}

	public void update() {

		// initialise elo objects
		for (String team : model.getTeamModel().getTeams()) {
			teamMap.put(team, new EloObject(team, 1500));
		}
		for (String player : model.getPlayerModel().getPlayers()) {
			playerMap.put(player, new EloObject(player, 1500));
		}

		// sort sets in the order relevant for elo ranking
		ObservableList<MatchSet> sets = sortMatchSets(model.getSets());

		// calculate elo ranking
		for (MatchSet set : sets) {
			updateElo(set);
		}

		teamElo.addAll(setPositions(teamMap.values()));
		playerElo.get().addAll(setPositions(playerMap.values()));
	}

	private ObservableList<EloObject> setPositions(Collection<EloObject> values) {
		Collection<EloObject> result = new TreeSet<EloObject>();
		int i = 1;
		for (EloObject elo : values) {
			elo.setPosition(i);
			i++;
		}
		result.addAll(values);
		return FXCollections.observableArrayList(result);
	}

	private void updateElo(MatchSet set) {
		String homePlayer1 = set.getHomePlayer1();
		String guestPlayer1 = set.getGuestPlayer1();
		if (Strings.isNullOrEmpty(homePlayer1)
				|| Strings.isNullOrEmpty(guestPlayer1)) {
			// games which are won without playing are not taken into
			// account.
			return;
		}
		EloObject homeP1Elo = playerMap.get(homePlayer1);
		EloObject guestP1Elo = playerMap.get(guestPlayer1);
		int eloDiff = 0;
		if (isSingle(set)) {
			eloDiff = computeElo(homeP1Elo.getElo(), guestP1Elo.getElo(),
					set.getHomeResult(), set.getGuestResult());
			homeP1Elo.setElo(homeP1Elo.getElo() + eloDiff);
			guestP1Elo.setElo(guestP1Elo.getElo() - eloDiff);
		} else {
			if (Strings.isNullOrEmpty(set.getHomePlayer2())
					|| Strings.isNullOrEmpty(set.getGuestPlayer2())) {
				// seems to be an odd game, ignore it.
				return;
			}
			EloObject homeP2Elo = playerMap.get(set.getHomePlayer2());
			EloObject guestP2Elo = playerMap.get(set.getGuestPlayer2());
			eloDiff = computeElo(
					(homeP1Elo.getElo() + homeP2Elo.getElo()) / 2d,
					(guestP1Elo.getElo() + guestP2Elo.getElo()) / 2d,
					set.getHomeResult(), set.getGuestResult());
			homeP1Elo.setElo(homeP1Elo.getElo() + eloDiff);
			guestP1Elo.setElo(guestP1Elo.getElo() - eloDiff);
			homeP2Elo.setElo(homeP2Elo.getElo() + eloDiff);
			guestP2Elo.setElo(guestP2Elo.getElo() - eloDiff);
		}
		EloObject homeTeam = teamMap.get(set.getHomeTeam());
		EloObject guestTeam = teamMap.get(set.getGuestTeam());
		homeTeam.setElo(homeTeam.getElo() + eloDiff);
		guestTeam.setElo(guestTeam.getElo() - eloDiff);
	}

	private boolean isSingle(MatchSet set) {
		return Strings.isNullOrEmpty(set.getHomePlayer2())
				&& Strings.isNullOrEmpty(set.getGuestPlayer2());
	}

	protected int computeElo(double elo1, double elo2, int result1, int result2) {
		double quot = (elo1 - elo2) / 400d;
		double ea = 1d / (1d + Math.pow(10d, quot));
		double hw = (Math.signum(result1 - result2) / 2d) + 0.5d;
		double eloDiff = eloFaktor * (hw - ea);
		return new Double(eloDiff).intValue();
	}

	public SortedList<EloObject> getTeamElo() {
		return teamElo.get().sorted(Ordering.<EloObject> natural());
	}

	public SortedList<EloObject> getPlayerElo() {
		return playerElo.get().sorted(Ordering.<EloObject> natural());
	}

	public SimpleListProperty<EloObject> playerEloProperty() {
		return playerElo;
	}

	public SimpleListProperty<EloObject> teamEloProperty() {
		return teamElo;
	}

	private ObservableList<MatchSet> sortMatchSets(ObservableList<MatchSet> sets) {
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
}
