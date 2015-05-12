package de.mt.poltool;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import de.mt.poltool.model.MatchSet;

public class CsvHandler {
	private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter
			.ofPattern("dd.MM.yyyy HH:mm");

	public Collection<MatchSet> read(File file) {
		Collection<MatchSet> matches = new TreeSet<MatchSet>();
		if (file != null) {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(file));
				List<String[]> lines = reader.readAll();
				for (String[] line : lines) {
					if (!line[0].equals("date")) {
						MatchSet set = new MatchSet();
						set.setDate(LocalDateTime.parse(line[0], DATE_PATTERN));
						set.setHomeTeam(line[1]);
						set.setGuestTeam(line[2]);
						set.setSetNr(Integer.parseInt(line[3]));
						set.setHomePlayer1(line[4]);
						set.setHomePlayer2(line[5]);
						set.setGuestPlayer1(line[6]);
						set.setGuestPlayer2(line[7]);
						set.setHomeResult(Integer.parseInt(line[8]));
						set.setGuestResult(Integer.parseInt(line[9]));
						if (line.length > 9) {
							set.setLeage(line[10]);
						}
						matches.add(set);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					reader = null;
				}
			}
		}
		return matches;
	}

	public void write(File file, Collection<MatchSet> matches) {
		CSVWriter csvWriter = null;
		try {
			Writer fileWriter = new FileWriter(file);
			csvWriter = new CSVWriter(fileWriter);
			csvWriter.writeNext(new String[] {//
					"date", " homeTeam", " guestTeam", " setNr", " homeP1",
							" homeP2", "guestP1", " guestP2", " homeResult",
							" guestResult", "leage" });
			for (MatchSet set : matches) {
				String homeTeam = set.getHomeTeam();
				String guestTeam = set.getGuestTeam();
				String date = set.getDate().format(DATE_PATTERN);

				String setNr = Integer.toString(set.getSetNr());
				String homeP1 = set.getHomePlayer1();
				String homeP2 = set.getHomePlayer2();
				String guestP1 = set.getGuestPlayer1();
				String guestP2 = set.getGuestPlayer2();
				String homeResult = Integer.toString(set.getHomeResult());
				String guestResult = Integer.toString(set.getGuestResult());
				String leage = set.getLeage();

				csvWriter.writeNext(new String[] {//
						date, homeTeam, guestTeam, setNr, homeP1, homeP2,
								guestP1, guestP2, homeResult, guestResult,
								leage });
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (csvWriter != null) {
				try {
					csvWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				csvWriter = null;
			}
		}
	}
}
