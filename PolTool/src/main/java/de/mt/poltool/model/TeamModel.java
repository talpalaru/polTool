package de.mt.poltool.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;

public class TeamModel {
	private ObservableList<String> teams;
	private Map<String, Collection<String>> teamPlayerMap;

	public TeamModel() {
		teams = FXCollections.observableArrayList();
		teamPlayerMap = new HashMap<String, Collection<String>>();
	}

	public void update(Collection<MatchSet> matchSets) {
		// get all teams
		Set<String> teamSet = new HashSet<String>();
		for (MatchSet set : matchSets) {
			teamSet.add(set.getHomeTeam());
			teamSet.add(set.getGuestTeam());
		}
		teamSet.add("");
		teams.setAll(teamSet);

		teamPlayerMap.clear();

		// assign players to teams
		for (MatchSet set : matchSets) {
			Collection<String> homeTeam = getTeamPlayers(set.getHomeTeam());
			if (!Strings.isNullOrEmpty(set.getHomePlayer1())) {
				homeTeam.add(set.getHomePlayer1());
			}
			if (!Strings.isNullOrEmpty(set.getHomePlayer2())) {
				homeTeam.add(set.getHomePlayer2());
			}
			Collection<String> guestTeam = getTeamPlayers(set.getGuestTeam());
			if (!Strings.isNullOrEmpty(set.getGuestPlayer1())) {
				guestTeam.add(set.getGuestPlayer1());
			}
			if (!Strings.isNullOrEmpty(set.getGuestPlayer2())) {
				guestTeam.add(set.getGuestPlayer2());
			}
		}

	}

	private Collection<String> getTeamPlayers(String team) {
		Collection<String> teamPlayers = teamPlayerMap.get(team);
		if (teamPlayers == null) {
			teamPlayers = new TreeSet<String>();
			teamPlayers.add("");
			teamPlayerMap.put(team, teamPlayers);
		}
		return teamPlayers;
	}

	public ObservableList<String> getTeams() {
		return teams.sorted(Ordering.<String> natural());
	}

	public Collection<String> getPlayersByTeam(String team) {
		return teamPlayerMap.get(team);
	}

}