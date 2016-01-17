package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.midi_automator.Resources;
import com.midi_automator.utils.FileUtils;

@Repository
public class Model implements IModel {

	static Logger log = Logger.getLogger(Model.class.getName());

	private final String DEFAULT_FILE_NAME = "file_list.mido";
	private final String VALUE_SEPARATOR = ";";

	@Autowired
	private SetList setList;

	@Autowired
	private Resources resources;

	@Override
	public String getPersistenceFileName() {
		return resources.getDefaultFileListPath() + DEFAULT_FILE_NAME;
	}

	@Override
	public void load() throws FileNotFoundException, IOException,
			TooManyEntriesException {

		setList.clear();
		FileReader fileReader = new FileReader(getPersistenceFileName());
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {

			String name = null;
			String filePath = null;
			String midiListeningSignature = null;
			String midiSendingSignature = null;
			String programPath = null;

			try {
				name = (line.split(VALUE_SEPARATOR))[0];
				filePath = (line.split(VALUE_SEPARATOR))[1];
				midiListeningSignature = (line.split(VALUE_SEPARATOR))[2];
				midiSendingSignature = (line.split(VALUE_SEPARATOR))[3];
				programPath = (line.split(VALUE_SEPARATOR))[4];
			} catch (IndexOutOfBoundsException e) {
				// catch empty csv values
			}

			SetListItem item = new SetListItem(name, filePath, programPath,
					midiListeningSignature, midiSendingSignature);

			setList.addItem(item);
		}
		bufferedReader.close();
		fileReader.close();

		if (setList.getItems().size() > 128) {
			throw new TooManyEntriesException();
		}
	}

	@Override
	public void save() throws FileNotFoundException, IOException {

		FileWriter fileWriter = new FileWriter(getPersistenceFileName());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		for (SetListItem item : setList.getItems()) {
			log.debug("Save item: " + item.getName() + " " + item.getFilePath());
			String csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR,
					item.getName(), item.getFilePath(),
					item.getMidiListeningSignature(),
					item.getMidiSendingSignature(), item.getProgramPath());
			log.debug("Save \"" + csvLine + "\"");
			bufferedWriter.write(csvLine);
			bufferedWriter.newLine();
		}

		log.debug("Save model to file");
		bufferedWriter.close();
		fileWriter.close();
	}

	@Override
	public SetList getSetList() {
		return setList;
	}

}
