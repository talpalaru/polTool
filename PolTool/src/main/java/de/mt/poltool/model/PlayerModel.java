package de.mt.poltool.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.google.common.collect.Ordering;

public class PlayerModel {
	private ObservableList<String> players;

	public PlayerModel() {
		players = FXCollections.observableArrayList();
	}

	public void update(Collection<MatchSet> matchSets) {
		// get all teams and players
		Set<String> playerSet = new HashSet<String>();
		for (MatchSet set : matchSets) {
			playerSet.add(set.getHomePlayer1());
			playerSet.add(set.getHomePlayer2());
			playerSet.add(set.getGuestPlayer1());
			playerSet.add(set.getGuestPlayer2());
		}
		playerSet.add("");
		players.setAll(playerSet);

	}

	public ObservableList<String> getPlayers() {
		return players.sorted(Ordering.<String> natural());
	}

}