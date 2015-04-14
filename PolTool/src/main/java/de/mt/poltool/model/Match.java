package de.mt.poltool.model;

import java.util.Collection;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.google.common.collect.ComparisonChain;

public class Match implements Comparable<Match> {

	private String leftTeam;
	private String rightTeam;
	private DateTime date;

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

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
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

	public int compareTo(Match o) {
		// TODO create sort order by date
		return ComparisonChain.start().compare(date, o.date).result();
	}

}
