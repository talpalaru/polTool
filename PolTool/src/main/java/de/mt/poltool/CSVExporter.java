package de.mt.poltool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.joda.time.format.DateTimeFormat;

import au.com.bytecode.opencsv.CSVWriter;
import de.mt.poltool.model.Match;
import de.mt.poltool.model.PlaySet;

public class CSVExporter {

	public void export(String fileName, Collection<Match> matches)
			throws IOException {
		CSVWriter csvWriter = null;
		try {
			Writer file = new FileWriter(fileName);
			csvWriter = new CSVWriter(file);
			csvWriter.writeNext(new String[] {//
					"date", " homeTeam", " guestTeam", " setNr", " homeP1",
							" homeP2", "guestP1", " guestP2", " homeResult",
							" guestResult" });
			for (Match match : matches) {
				String homeTeam = match.getLeftTeam();
				String guestTeam = match.getRightTeam();
				String date = match.getDate().toString(
						DateTimeFormat.forPattern("dd.MM.yyyy HH:mm"));

				for (PlaySet set : match.getSets()) {
					String setNr = Integer.toString(set.getSetNr());
					String homeP1 = set.getLeftPlayer1();
					String homeP2 = set.getLeftPlayer2();
					String guestP1 = set.getRightPlayer1();
					String guestP2 = set.getRightPlayer1();
					String homeResult = Integer.toString(set.getLeftResult());
					String guestResult = Integer.toString(set.getRightResult());

					csvWriter.writeNext(new String[] {//
							date, homeTeam, guestTeam, setNr, homeP1, homeP2,
									guestP1, guestP2, homeResult, guestResult });
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (csvWriter != null) {
				csvWriter.close();
				csvWriter = null;
			}
		}
	}
}
