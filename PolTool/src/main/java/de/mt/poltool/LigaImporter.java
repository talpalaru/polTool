package de.mt.poltool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.mt.poltool.model.Match;
import de.mt.poltool.model.PlaySet;

public class LigaImporter {

	public Collection<Match> fetchMatchesFromTeamOverviewSite(String sourceUrl)
			throws Exception {
		Collection<Match> matches = new ArrayList<Match>();
		Document site = getDocument(sourceUrl);
		Elements links = site.select("body a");
		Map<String, String> teams = new HashMap<String, String>();
		for (Element link : links) {
			if (link.attr("href").contains(
					"/liga-tool/mannschaften?task=team_details")) {
				teams.put(link.text(), link.attr("abs:href"));
			}
		}
		System.out.println("Found " + teams.size() + " teams.\n");
		for (Entry<String, String> team : teams.entrySet()) {
			System.out.println("Adding: " + team.getKey());
			matches.addAll(fetchMatchesFromTeamSite(team.getValue()));
		}

		return matches;
	}

	/**
	 * Fetches all matches directly linked on the given team site.
	 * 
	 * @param sourceUrl
	 * @return
	 * @throws Exception
	 */
	public Collection<Match> fetchMatchesFromTeamSite(String sourceUrl)
			throws Exception {
		Collection<Match> matches = new ArrayList<Match>();
		Document teamSite = getDocument(sourceUrl);
		Elements links = teamSite.select("body a");
		for (Element link : links) {
			if (link.attr("href").contains(
					"/liga-tool/mannschaften?task=begegnung_spielplan")) {
				Match match = fetchMatchFromMatchSite(link.attr("abs:href"));
				match.setDate(DateTime.parse(link.text().split(",")[1].trim(),
						DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")));
				matches.add(match);
			}
		}

		return matches;
	}

	/**
	 * Fetches the match results from the given site.
	 * 
	 * @param sourceUrl
	 * @return
	 * @throws Exception
	 */
	public Match fetchMatchFromMatchSite(String sourceUrl) throws Exception {
		Match match = new Match();

		Document doc = getDocument(sourceUrl);
		fetchTeamNames(match, doc);
		match.addSets(parseRows(doc));
		return match;

	}

	/**
	 * Parses a table row containing a set. Player, result and set nr are
	 * extracted.
	 * 
	 * @param rowElements
	 * @return
	 */
	private Collection<PlaySet> parseRows(Document doc) {
		Elements rowElements = doc.select("body .sectiontableentry1");
		rowElements.addAll(doc.select("body .sectiontableentry2"));
		Collection<PlaySet> sets = new TreeSet<PlaySet>();
		for (Element row : rowElements) {
			PlaySet set = new PlaySet();

			Elements cells = row.select(" > td");
			// set Number
			set.setSetNr(Integer.parseInt(cells.get(0).text()));

			// result
			String resultText = fetchResultText(row);
			String[] results = resultText.trim().split(":");
			set.setLeftResult(Integer.parseInt(results[0]));
			set.setRightResult(Integer.parseInt(results[1]));

			// player names and single or double game
			Elements leftTeam = null;
			Elements rightTeam = null;
			if (cells.size() == 4) {
				leftTeam = cells.get(1).getElementsByTag("a");
				rightTeam = cells.get(3).getElementsByTag("a");

			} else if (cells.size() == 6) {
				leftTeam = cells.get(2).getElementsByTag("a");
				rightTeam = cells.get(4).getElementsByTag("a");
			} else {
				System.err.println("Unkown table row format in row: "
						+ row.html());
			}

			set.setSingle(leftTeam.size() < 2 && rightTeam.size() < 2);
			if (set.isSingle()) {
				set.setLeftPlayer1(leftTeam.get(0).text());
				set.setRightPlayer1(rightTeam.get(0).text());
			} else {
				if (leftTeam.size() > 0) {
					set.setLeftPlayer1(leftTeam.get(0).text());
				}
				if (leftTeam.size() > 1) {
					set.setLeftPlayer2(leftTeam.get(1).text());
				}
				if (leftTeam.size() > 0) {
					set.setRightPlayer1(rightTeam.get(0).text());
				}
				if (leftTeam.size() > 1) {
					set.setRightPlayer2(rightTeam.get(1).text());
				}
			}

			//
			// // player names and single or double game
			// Elements players = fetchPlayers(row);
			// switch (players.size()) {
			// case 2:
			// set.setSingle(true);
			// set.setLeftPlayer1(players.get(0).text());
			// set.setRightPlayer1(players.get(1).text());
			// break;
			// case 4:
			// set.setSingle(false);
			// set.setLeftPlayer1(players.get(0).text());
			// set.setLeftPlayer2(players.get(1).text());
			// set.setRightPlayer1(players.get(2).text());
			// set.setRightPlayer2(players.get(3).text());
			// break;
			//
			// default:
			// System.err.println("Number of Players in a set is"
			// + players.size() + "?? in row: " + row.html());
			// continue;
			// }

			sets.add(set);
		}
		return sets;
	}

	private String fetchResultText(Element row) {
		Elements resultElement = row
				.getElementsMatchingOwnText("[\\d]+:[\\d]+");
		if (resultElement.size() != 1) {
			System.err.println("More than one result found in row: "
					+ row.html());
			return "0:0";
		}
		return resultElement.get(0).text();
	}

	private Elements fetchPlayers(Element row) {
		return row.getElementsByTag("a");
	}

	private Document getDocument(String sourceUrl) throws IOException {
		Connection connect = Jsoup.connect(sourceUrl);
		connect.timeout(0);
		Document doc = connect.get();
		return doc;
	}

	private void fetchTeamNames(Match playList, Document document) {
		Elements tableHeader = document.select("body .sectiontableheader");
		for (Element e : tableHeader) {
			String text = e.text();
			if (text.contains("vs.")) {
				String[] sections = text.split(">");
				String[] teams = sections[sections.length - 1].split("vs.");
				playList.setLeftTeam(teams[0].trim());
				playList.setRightTeam(teams[1].trim());
			}
		}
	}
}
