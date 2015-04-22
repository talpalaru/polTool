package de.mt.poltool.gui.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class GuiModel implements ListChangeListener<MatchSet> {

	private ObservableList<MatchSet> matcheSets;
	private ObservableList<String> teams;
	private ObservableList<String> players;

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
	}

	public ObservableList<String> getTeams() {
		return new SortedList<String>(teams, (s1, s2) -> s1.compareTo(s2));
	}

	public ObservableList<String> getPlayers() {
		return new SortedList<String>(players, (s1, s2) -> s1.compareTo(s2));
	}

	@Override
	public void onChanged(
			javafx.collections.ListChangeListener.Change<? extends MatchSet> c) {
		Set<String> teamSet = new HashSet<String>();
		for (MatchSet set : matcheSets) {
			teamSet.add(set.getHomeTeam());
			teamSet.add(set.getGuestTeam());
		}
		teamSet.add("");
		teams.setAll(teamSet);
		Set<String> playerSet = new HashSet<String>();
		for (MatchSet set : matcheSets) {
			playerSet.add(set.getHomePlayer1());
			playerSet.add(set.getHomePlayer2());
			playerSet.add(set.getGuestPlayer2());
		}
		playerSet.add("");
		players.setAll(playerSet);
	}
}
