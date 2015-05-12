package de.mt.poltool.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class EloObject implements Comparable<EloObject> {

	private String name;
	private int elo;
	private int position;
	private String leage;

	public EloObject(String name, int elo) {
		super();
		this.name = name;
		this.elo = elo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getElo() {
		return elo;
	}

	public void setElo(int elo) {
		this.elo = elo;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getLeage() {
		return leage;
	}

	public void setLeage(String leage) {
		this.leage = leage;
	}

	@Override
	public int compareTo(EloObject o) {
		if (o == null)
			return 1;
		return ComparisonChain
				.start()
				.compare(this.getElo(), o.getElo())
				.compare(this.getLeage(), o.getLeage(),
						Ordering.<String> natural().nullsLast())
				.compare(this.getName(), o.getName(),
						Ordering.<String> natural().nullsLast()).result();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + elo;
		result = prime * result + ((leage == null) ? 0 : leage.hashCode());
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
		EloObject other = (EloObject) obj;
		if (elo != other.elo)
			return false;
		if (leage == null) {
			if (other.leage != null)
				return false;
		} else if (!leage.equals(other.leage))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
