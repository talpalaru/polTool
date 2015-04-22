package de.mt.poltool.gui.model;

import java.time.LocalDateTime;
import java.util.Collection;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class MatchSet implements Comparable<MatchSet> {
	private String homeTeam;
	private String guestTeam;
	private LocalDateTime date;
	private int setNr;
	private String homePlayer1;
	private String guestPlayer1;
	private String homePlayer2;
	private String guestPlayer2;
	private int homeResult;
	private int guestResult;

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(String guestTeam) {
		this.guestTeam = guestTeam;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public int getSetNr() {
		return setNr;
	}

	public void setSetNr(int setNr) {
		this.setNr = setNr;
	}

	public String getHomePlayer1() {
		return homePlayer1;
	}

	public void setHomePlayer1(String homePlayer1) {
		this.homePlayer1 = homePlayer1;
	}

	public String getGuestPlayer1() {
		return guestPlayer1;
	}

	public void setGuestPlayer1(String guestPlayer1) {
		this.guestPlayer1 = guestPlayer1;
	}

	public String getHomePlayer2() {
		return homePlayer2;
	}

	public void setHomePlayer2(String homePlayer2) {
		this.homePlayer2 = homePlayer2;
	}

	public String getGuestPlayer2() {
		return guestPlayer2;
	}

	public void setGuestPlayer2(String guestPlayer2) {
		this.guestPlayer2 = guestPlayer2;
	}

	public int getHomeResult() {
		return homeResult;
	}

	public void setHomeResult(int homeResult) {
		this.homeResult = homeResult;
	}

	public int getGuestResult() {
		return guestResult;
	}

	public void setGuestResult(int guestResult) {
		this.guestResult = guestResult;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((guestTeam == null) ? 0 : guestTeam.hashCode());
		result = prime * result
				+ ((homeTeam == null) ? 0 : homeTeam.hashCode());
		result = prime * result + setNr;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchSet other = (MatchSet) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (guestTeam == null) {
			if (other.guestTeam != null)
				return false;
		} else if (!guestTeam.equals(other.guestTeam))
			return false;
		if (homeTeam == null) {
			if (other.homeTeam != null)
				return false;
		} else if (!homeTeam.equals(other.homeTeam))
			return false;
		if (setNr != other.setNr)
			return false;
		return true;
	}

	public int compareTo(MatchSet o) {

		return ComparisonChain.start().//
				compare(date, o.date).//
				compare(homeTeam, o.homeTeam).//
				compare(guestTeam, o.guestTeam).//
				compare(setNr, o.setNr).//
				result();
	}

	public Collection<String> getTeams() {
		return Lists.<String> newArrayList(getHomeTeam(), getGuestTeam());
	}

	public Collection<String> getPlayers() {
		return Lists.<String> newArrayList(getHomePlayer1(), getHomePlayer2(),
				getGuestPlayer1(), getGuestPlayer2());
	}
}
