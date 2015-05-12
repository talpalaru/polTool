package de.mt.poltool.gui;

import javafx.collections.ObservableList;

import com.google.common.base.Strings;

import de.mt.poltool.model.EloObject;
import de.mt.poltool.model.GuiModel;
import de.mt.poltool.model.MatchSet;

public class PlayerEloTable extends AbstractEloTable {

	public PlayerEloTable(GuiModel model) {
		super(model);
		names = model.getPlayerModel().getPlayers();
	}

	public void createTable() {
		createColumn("Rang", "position", Integer.class, 40d);
		createColumn("Spieler", "name", String.class, 200d);
		createColumn("ELO", "elo", Integer.class, 100d);
	}

	@Override
	protected void updateElos(ObservableList<MatchSet> sets) {
		// calculate elo ranking
		for (MatchSet set : sets) {
			String homePlayer1 = set.getHomePlayer1();
			String guestPlayer1 = set.getGuestPlayer1();
			if (Strings.isNullOrEmpty(homePlayer1)
					|| Strings.isNullOrEmpty(guestPlayer1)) {
				// games which are won without playing are not taken into
				// account.
				continue;
			}
			if (isSingle(set)) {
				updateElo(homePlayer1, guestPlayer1, set.getHomeResult(),
						set.getGuestResult());
			} else {
				String guestPlayer2 = set.getGuestPlayer2();
				String homePlayer2 = set.getHomePlayer2();
				updateDoubleElo(homePlayer1, homePlayer2, guestPlayer1,
						guestPlayer2, set.getHomeResult(), set.getGuestResult());
			}
		}
	}

	private boolean isSingle(MatchSet set) {
		return Strings.isNullOrEmpty(set.getHomePlayer2())
				&& Strings.isNullOrEmpty(set.getGuestPlayer2());
	}

	private void updateDoubleElo(String homePlayer1, String homePlayer2,
			String guestPlayer1, String guestPlayer2, int homeResult,
			int guestResult) {
		EloObject home1 = get(homePlayer1);
		EloObject guest1 = get(guestPlayer1);
		EloObject home2 = get(homePlayer2);
		EloObject guest2 = get(guestPlayer2);
		int eloDiff = computeElo(home1.getElo() + home2.getElo(),
				guest1.getElo() + guest2.getElo(), homeResult, guestResult);
		int halfEloDiff = eloDiff / 2;

		home1.setElo(home1.getElo() + halfEloDiff);
		home2.setElo(home2.getElo() + halfEloDiff);
		guest1.setElo(guest1.getElo() - halfEloDiff);
		guest2.setElo(guest2.getElo() - halfEloDiff);
	}

}
