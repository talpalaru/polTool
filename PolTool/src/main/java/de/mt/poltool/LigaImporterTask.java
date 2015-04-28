package de.mt.poltool;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.concurrent.Task;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;

import de.mt.poltool.model.MatchSet;

public class LigaImporterTask extends Task<Collection<MatchSet>> {

	private String url;
	private Type type;

	public Collection<MatchSet> fetchMatchesFromTeamOverviewSite(
			String sourceUrl) {
		Collection<MatchSet> matches = new TreeSet<MatchSet>();
		Document site;
		try {
			site = getDocument(sourceUrl);
			Elements links = site.select("body a");
			Map<String, String> teams = new TreeMap<String, String>();
			for (Element link : links) {
				if (link.attr("href").contains(
						"/liga-tool/mannschaften?task=team_details")) {
					teams.put(link.text(), link.attr("abs:href"));
				}
			}
			int teamCount = teams.size();
			updateMessage(teamCount + " Teams gefunden.");
			System.out.println(teamCount + " Teams gefunden.");
			int i = 0;
			for (Entry<String, String> team : teams.entrySet()) {
				updateMessage(i + " von " + teamCount + ": " + team.getKey()
						+ " eingelesen.");
				updateProgress(i, teamCount);
				System.out.println(i + " von " + teamCount + ": "
						+ team.getKey() + " eingelesen.");
				matches.addAll(fetchMatchesFromTeamSite(team.getValue()));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
	public Collection<MatchSet> fetchMatchesFromTeamSite(String sourceUrl) {
		Collection<MatchSet> matches = new ArrayList<MatchSet>();
		Document teamSite;
		try {
			teamSite = getDocument(sourceUrl);
			Elements links = teamSite.select("body a");
			for (Element link : links) {
				if (link.attr("href").contains(
						"/liga-tool/mannschaften?task=begegnung_spielplan")) {

					matches.addAll(fetchMatchFromMatchSite(link
							.attr("abs:href")));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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
	public Collection<MatchSet> fetchMatchFromMatchSite(String sourceUrl) {
		Collection<MatchSet> matches = new ArrayList<MatchSet>();

		Document doc;
		try {
			doc = getDocument(sourceUrl);

			Elements tableHeader = doc.select("body .sectiontableheader");
			String homeTeam = "";
			String guestTeam = "";
			for (Element e : tableHeader) {
				String text = e.text();
				if (text.contains("vs.")) {
					String[] sections = text.split(">");
					String[] teams = sections[sections.length - 1].split("vs.");
					homeTeam = teams[0].trim();
					guestTeam = teams[1].trim();
				}
			}
			Elements dateElements = doc
					.getElementsMatchingOwnText(".(Spieltag|Runde).*");
			LocalDateTime date = null;
			if (!dateElements.isEmpty()) {
				String dateText = dateElements.get(0).text();
				if (!Strings.isNullOrEmpty(dateText)) {
					date = LocalDateTime.parse(dateText.split(",")[1].trim(),
							DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
				}
			}
			matches.addAll(parseRows(doc));
			for (MatchSet matchSet : matches) {
				matchSet.setHomeTeam(homeTeam);
				matchSet.setGuestTeam(guestTeam);
				matchSet.setDate(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return matches;

	}

	/**
	 * Parses a table row containing a set. Player, result and set nr are
	 * extracted.
	 * 
	 * @param rowElements
	 * @return
	 */
	private Collection<MatchSet> parseRows(Document doc) {
		Elements rowElements = doc.select("body .sectiontableentry1");
		rowElements.addAll(doc.select("body .sectiontableentry2"));
		Collection<MatchSet> sets = new ArrayList<MatchSet>();
		for (Element row : rowElements) {
			MatchSet set = new MatchSet();

			Elements cells = row.select(" > td");
			// set Number
			set.setSetNr(Integer.parseInt(cells.get(0).text()));

			// result
			String resultText = fetchResultText(row);
			String[] results = resultText.trim().split(":");
			set.setHomeResult(Integer.parseInt(results[0]));
			set.setGuestResult(Integer.parseInt(results[1]));

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

			// first players
			if (leftTeam.size() > 0) {
				set.setHomePlayer1(leftTeam.get(0).text());
			}
			if (rightTeam.size() > 0) {
				set.setGuestPlayer1(rightTeam.get(0).text());
			}

			// if double set second players
			if (leftTeam.size() > 1) {
				set.setHomePlayer2(leftTeam.get(1).text());
			}
			if (rightTeam.size() > 1) {
				set.setGuestPlayer2(rightTeam.get(1).text());
			}

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

	private Document getDocument(String sourceUrl) throws IOException {
		Connection connect = Jsoup.connect(sourceUrl);
		connect.timeout(0);
		Document doc = connect.get();
		return doc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	protected Collection<MatchSet> call() throws Exception {
		switch (type) {
		case OVERVIEW:
			return fetchMatchesFromTeamOverviewSite(url);
		case TEAM:
			return fetchMatchesFromTeamSite(url);
		case MATCH:
			return fetchMatchFromMatchSite(url);
		}
		return new ArrayList<MatchSet>();
	}

	public enum Type {
		OVERVIEW, TEAM, MATCH
	}

}
