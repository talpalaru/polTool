package de.mt.poltool.model;

import com.google.common.collect.ComparisonChain;

public class PlaySet implements Comparable<PlaySet> {
	private int setNr;
	private boolean isSingle = true;
	private String leftPlayer1;
	private String rightPlayer1;
	private String leftPlayer2;
	private String rightPlayer2;
	private int leftResult;
	private int rightResult;

	public int getSetNr() {
		return setNr;
	}

	public void setSetNr(int satzNr) {
		this.setNr = satzNr;
	}

	public int getLeftResult() {
		return leftResult;
	}

	public void setLeftResult(int leftResult) {
		this.leftResult = leftResult;
	}

	public int getRightResult() {
		return rightResult;
	}

	public void setRightResult(int rightResult) {
		this.rightResult = rightResult;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public String getLeftPlayer1() {
		return leftPlayer1;
	}

	public void setLeftPlayer1(String leftPlayer1) {
		this.leftPlayer1 = leftPlayer1;
	}

	public String getRightPlayer1() {
		return rightPlayer1;
	}

	public void setRightPlayer1(String rightPlayer1) {
		this.rightPlayer1 = rightPlayer1;
	}

	public String getLeftPlayer2() {
		return leftPlayer2;
	}

	public void setLeftPlayer2(String leftPlayer2) {
		this.leftPlayer2 = leftPlayer2;
	}

	public String getRightPlayer2() {
		return rightPlayer2;
	}

	public void setRightPlayer2(String rightPlayer2) {
		this.rightPlayer2 = rightPlayer2;
	}

	public int compareTo(PlaySet o) {
		return ComparisonChain.start().compare(this.getSetNr(), o.getSetNr())
				.result();
	}

	@Override
	public String toString() {
		return (isSingle ? "Single" : "Double") + " Set:" + setNr + " "
				+ leftResult + " to " + rightResult
				+ " with "//
				+ leftPlayer1 + (!isSingle ? " " + leftPlayer2 : "") + " vs. "
				+ rightPlayer1 + (!isSingle ? " " + rightPlayer2 : "");
	}
}
