package de.mt.poltool.gui.model;

import java.io.File;

public class ReadCsvModel {
	private GuiModel matchModel;
	private File file;

	public ReadCsvModel(GuiModel model) {
		matchModel = model;
	}

	public GuiModel getMatchModel() {
		return matchModel;
	}

	public void setMatchModel(GuiModel matchModel) {
		this.matchModel = matchModel;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
