package de.mt.poltool.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;

public class Match implements Comparable<Match> {

	private String leftTeam;
	private String rightTeam;
	private LocalDateTime date;

	private Collection<PlaySet> sets = new TreeSet<PlaySet>();

	public Collection<PlaySet> getSets() {
		return sets;
	}

	public void addSet(PlaySet set) {
		sets.add(set);
	}

	public void addSets(Collection<PlaySet> set) {
		sets.addAll(set);
	}

	public String getLeftTeam() {
		return leftTeam;
	}

	public void setLeftTeam(String leftTeam) {
		this.leftTeam = leftTeam;
	}

	public String getRightTeam() {
		return rightTeam;
	}

	public void setRightTeam(String rightTeam) {
		this.rightTeam = rightTeam;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(leftTeam);
		sb.append(" against ");
		sb.append(rightTeam);
		sb.append(" at ");
		sb.append(date);
		sb.append("\n\n");
		for (PlaySet playSet : sets) {
			sb.append(playSet.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((leftTeam == null) ? 0 : leftTeam.hashCode());
		result = prime * result
				+ ((rightTeam == null) ? 0 : rightTeam.hashCode());
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
		Match other = (Match) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (leftTeam == null) {
			if (other.leftTeam != null)
				return false;
		} else if (!leftTeam.equals(other.leftTeam))
			return false;
		if (rightTeam == null) {
			if (other.rightTeam != null)
				return false;
		} else if (!rightTeam.equals(other.rightTeam))
			return false;
		return true;
	}

	public int compareTo(Match o) {
		return ComparisonChain.start().//
				compare(date, o.date).//
				compare(leftTeam, o.leftTeam).//
				compare(rightTeam, o.rightTeam).//
				result();
	}

}
