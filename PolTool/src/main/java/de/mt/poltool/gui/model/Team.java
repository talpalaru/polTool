package de.mt.poltool.gui.model;

import java.util.Collection;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;

public class Team implements Comparable<Team> {

	private String name;

	private Collection<Player> players;

	public Team(String name) {
		this.name = name;
		players = new TreeSet<Player>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	@Override
	public int compareTo(Team o) {
		return o == null ? 1 : ComparisonChain.start()
				.compare(name, o.getName()).result();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Team other = (Team) obj;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}

}
