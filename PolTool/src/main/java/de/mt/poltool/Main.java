package de.mt.poltool;

import java.util.ArrayList;
import java.util.Collection;

import de.mt.poltool.model.Match;
import de.mt.poltool.visualisation.TeamGraph;

public class Main {

	private static String[] matchSites = new String[] {//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=begegnung_spielplan&veranstaltungid=73&teamid=1105&id=4908",//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=begegnung_spielplan&veranstaltungid=93&teamid=1424&id=6242" };

	private static String[] jets = new String[] {//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=95&id=1391"//
			,
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=76&id=1003",//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=64&id=799",//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=47&id=501"//
	};
	private static String[] kickeronis = new String[] {//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=95&id=1445",
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=83&id=1256" };

	private static String[] kickerbande = new String[] {//
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=98&id=1455",
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=75&id=946",
			"http://kickern-hamburg.de/liga-tool/mannschaften?task=team_details&veranstaltungid=64&id=800" };

	public static void main(String[] args) throws Exception {
		LigaImporter li = new LigaImporter();
		Collection<Match> matches = new ArrayList<Match>();
		// matches.addAll(li
		// .fetchMatchesFromTeamOverviewSite("http://kickern-hamburg.de/liga-tool/mannschaften"));
		matches.addAll(li.fetchMatchesFromTeamSite(kickeronis[0]));
		// matches.add(li.fetchMatchFromMatchSite(matchSites[1]));

		// new CSVExporter().export("c:\\tmp\\liga2015.csv", matches);
		new TeamGraph().showWinsPerSetNr(matches);
	}
}
