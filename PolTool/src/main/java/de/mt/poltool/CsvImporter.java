package de.mt.poltool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import au.com.bytecode.opencsv.CSVReader;
import de.mt.poltool.gui.model.MatchSet;

public class CsvImporter {
	//
	public Collection<MatchSet> read(File file) {
		Collection<MatchSet> matches = new TreeSet<MatchSet>();
		if (file != null) {
			try {
				CSVReader reader = new CSVReader(new FileReader(file));
				List<String[]> lines = reader.readAll();
				for (String[] line : lines) {
					if (!line[0].equals("date")) {
						MatchSet set = new MatchSet();
						set.setDate(LocalDateTime
								.parse(line[0], DateTimeFormatter
										.ofPattern("dd.MM.yyyy HH:mm")));
						set.setHomeTeam(line[1]);
						set.setGuestTeam(line[2]);
						set.setSetNr(Integer.parseInt(line[3]));
						set.setHomePlayer1(line[4]);
						set.setHomePlayer2(line[5]);
						set.setGuestPlayer1(line[6]);
						set.setGuestPlayer2(line[7]);
						set.setHomeResult(Integer.parseInt(line[8]));
						set.setGuestResult(Integer.parseInt(line[9]));
						matches.add(set);
					}
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return matches;
	}

}
