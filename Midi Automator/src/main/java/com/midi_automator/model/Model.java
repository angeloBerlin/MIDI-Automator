package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
public class Model {

	static Logger log = Logger.getLogger(Model.class.getName());

	private final String VALUE_SEPARATOR = ";";
	public static final char COMMENT_SIGN = '#';

	@Autowired
	private SetList setList;

	@Autowired
	private Resources resources;

	/**
	 * Gets the file name of the model
	 * 
	 * @return The name of the persisting file
	 */
	public String getPersistenceFileName() {
		return resources.getDefaultFileListPath() + File.separator
				+ resources.getDefaultFileName();
	}

	/**
	 * Loads the persistent file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TooManyEntriesException
	 */
	public void load() throws FileNotFoundException, IOException,
			TooManyEntriesException {

		setList.clear();
		String fileName = getPersistenceFileName();
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {

			String name = null;
			String filePath = null;
			String midiListeningSignature = null;
			String midiSendingSignature = null;
			String programPath = null;

			if (line.charAt(0) != COMMENT_SIGN) {
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
		}
		bufferedReader.close();
		fileReader.close();

		if (setList.getItems().size() > 128) {
			throw new TooManyEntriesException();
		}
	}

	/**
	 * Saves to the persistent file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void save() throws FileNotFoundException, IOException {

		FileWriter fileWriter = new FileWriter(getPersistenceFileName());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write(COMMENT_SIGN + " Midi Automator Version: "
				+ resources.getVersion());
		bufferedWriter.newLine();

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

		log.debug("Saved model to file");
		bufferedWriter.close();
		fileWriter.close();
	}

	/**
	 * Gets the set list
	 * 
	 * @return a SetList object
	 */
	public SetList getSetList() {
		return setList;
	}

	/**
	 * Migrates the properties file to the current version
	 */
	public void migrate() {
		try {
			save();
		} catch (FileNotFoundException e) {
			log.error(getPersistenceFileName() + " not found for migration.", e);
		} catch (IOException e) {
			log.error("Storing migration of " + getPersistenceFileName()
					+ " failed.", e);
		}
	}
}
