package de.mt.poltool.gui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;

public class GuiModel implements ListChangeListener<MatchSet> {

	private ObservableList<MatchSet> matcheSets;
	private ObservableList<String> teams;
	private ObservableList<String> players;
	private Map<String, Collection<String>> teamPlayerMap;

	public GuiModel() {
		matcheSets = FXCollections.observableArrayList();
		matcheSets.addListener(this);
		teams = FXCollections.observableArrayList();
		players = FXCollections.observableArrayList();
	}

	public GuiModel(Collection<MatchSet> matchSets) {
		this();
		this.matcheSets.addAll(matchSets);
	}

	public void addMatchSets(Collection<MatchSet> matchSets) {
		this.matcheSets.addAll(matchSets);
	}

	public void addMatchSet(MatchSet matchSet) {
		this.matcheSets.add(matchSet);
	}

	public Collection<MatchSet> getSetsForTeam(final String team) {
		return Collections2.filter(matcheSets, new Predicate<MatchSet>() {

			public boolean apply(MatchSet set) {
				return team.equals(set.getHomeTeam())
						|| team.equals(set.getGuestTeam());
			}
		});
	}

	public ObservableList<MatchSet> getSets() {
		return matcheSets;
	}

	public void clear() {
		matcheSets.clear();
		teams.clear();
		players.clear();
	}

	public ObservableList<String> getTeams() {
		return new SortedList<String>(teams, (s1, s2) -> {
			if (s1 == null) {
				return s2 == null ? 0 : 1;
			}
			if (s2 == null) {
				return 1;
			}
			return s1.compareTo(s2);
		});
	}

	public ObservableList<String> getPlayers() {
		return new SortedList<String>(players, (s1, s2) -> {
			if (s1 == null) {
				return s2 == null ? 0 : 1;
			}
			if (s2 == null) {
				return 1;
			}
			return s1.compareTo(s2);
		});
	}

	public Collection<String> getPlayersByTeam(String team) {
		return teamPlayerMap.get(team);
	}

	@Override
	public void onChanged(
			javafx.collections.ListChangeListener.Change<? extends MatchSet> c) {
		Set<String> teamSet = new HashSet<String>();
		Set<String> playerSet = new HashSet<String>();
		for (MatchSet set : matcheSets) {
			teamSet.add(set.getHomeTeam());
			teamSet.add(set.getGuestTeam());
			playerSet.add(set.getHomePlayer1());
			playerSet.add(set.getHomePlayer2());
			playerSet.add(set.getGuestPlayer1());
			playerSet.add(set.getGuestPlayer2());
		}
		teamSet.add("");
		teams.setAll(teamSet);
		playerSet.add("");
		players.setAll(playerSet);
		calculateTeams();
	}

	private void calculateTeams() {
		teamPlayerMap = new HashMap<String, Collection<String>>();
		for (String team : teams) {
			if (!Strings.isNullOrEmpty(team)) {
				TreeSet<String> pl = new TreeSet<String>();
				pl.add("");
				teamPlayerMap.put(team, pl);
			}
		}
		for (MatchSet set : matcheSets) {
			Collection<String> homeTeam = teamPlayerMap.get(set.getHomeTeam());
			if (!Strings.isNullOrEmpty(set.getHomePlayer1())) {
				homeTeam.add(set.getHomePlayer1());
			}
			if (!Strings.isNullOrEmpty(set.getHomePlayer2())) {
				homeTeam.add(set.getHomePlayer2());
			}
			Collection<String> guestTeam = teamPlayerMap
					.get(set.getGuestTeam());
			if (!Strings.isNullOrEmpty(set.getGuestPlayer1())) {
				guestTeam.add(set.getGuestPlayer1());
			}
			if (!Strings.isNullOrEmpty(set.getGuestPlayer2())) {
				guestTeam.add(set.getGuestPlayer2());
			}
		}

	}
}
