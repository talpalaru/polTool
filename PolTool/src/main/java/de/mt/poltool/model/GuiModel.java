package de.mt.poltool.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;

public class GuiModel implements ListChangeListener<MatchSet> {

	private ObservableList<MatchSet> matchSets;
	private TeamModel teamModel;
	private PlayerModel playerModel;
	private EloModel eloModel;
	private ObservableList<String> leages;

	public GuiModel() {
		matchSets = FXCollections.observableArrayList();
		matchSets.addListener(this);
		teamModel = new TeamModel();
		playerModel = new PlayerModel();
		eloModel = new EloModel(this);
		leages = FXCollections.observableArrayList();
	}

	public GuiModel(Collection<MatchSet> matchSets) {
		this();
		this.matchSets.addAll(matchSets);
	}

	public void addMatchSets(Collection<MatchSet> matchSets) {
		this.matchSets.addAll(matchSets);
	}

	public ObservableList<MatchSet> getSets() {
		return matchSets;
	}

	public TeamModel getTeamModel() {
		return teamModel;
	}

	public ObservableList<String> getLeages() {
		return leages.sorted(Ordering.<String> natural());
	}

	public Collection<MatchSet> getSetsForTeam(final String team) {
		return Collections2.filter(matchSets, new Predicate<MatchSet>() {

			public boolean apply(MatchSet set) {
				return team.equals(set.getHomeTeam())
						|| team.equals(set.getGuestTeam());
			}
		});
	}

	public void clear() {
		matchSets.clear();
		teamModel.getTeams().clear();
		playerModel.getPlayers().clear();
		leages.clear();
	}

	@Override
	public void onChanged(
			javafx.collections.ListChangeListener.Change<? extends MatchSet> c) {
		Set<String> leageSet = new HashSet<String>();
		for (MatchSet set : matchSets) {
			leageSet.add(set.getLeage());
		}
		leages.addAll(leageSet);
		teamModel.update(matchSets);
		playerModel.update(matchSets);
		eloModel.update();
	}

	public PlayerModel getPlayerModel() {
		return playerModel;
	}

	public EloModel getEloModel() {
		return eloModel;
	}

}
