package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.midi_automator.Resources;
import com.midi_automator.utils.FileUtils;

public class Model implements IModel {

	static Logger log = Logger.getLogger(Model.class.getName());

	private static Model instance;
	private String persistenceFileName;
	private final String DEFAULT_FILE_NAME = "file_list.mido";
	private final String VALUE_SEPARATOR = ";";
	private SetList setList;
	private ApplicationContext ctx;

	/**
	 * Private constructor for singleton pattern
	 * 
	 */
	private Model() {
		ctx = new ClassPathXmlApplicationContext("Beans.xml");
	}

	/**
	 * Private constructor for singleton pattern
	 * 
	 * @param resources
	 *            The resources file
	 */
	private Model(Resources resources) {
		this();
		persistenceFileName = resources.getDefaultFileListPath()
				+ DEFAULT_FILE_NAME;
	}

	/**
	 * Gets the unique instance of the model
	 * 
	 * @param resources
	 *            The resources file
	 * @return Singleton instance
	 */
	public static Model getInstance(Resources resources) {

		if (instance == null) {
			instance = new Model(resources);
		}

		return instance;
	}

	@Override
	public void setPersistenceFileName(String fileName) {

		if (fileName != null) {
			persistenceFileName = fileName;
		}
	}

	@Override
	public String getPersistenceFileName() {
		return persistenceFileName;
	}

	@Override
	public void load() throws FileNotFoundException, IOException,
			TooManyEntriesException {

		setList.clear();
		FileReader fileReader = new FileReader(persistenceFileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {

			String name = null;
			String filePath = null;
			String midiListeningSignature = null;
			String midiSendingSignature = null;

			try {
				name = (line.split(VALUE_SEPARATOR))[0];
				filePath = (line.split(VALUE_SEPARATOR))[1];
				midiListeningSignature = (line.split(VALUE_SEPARATOR))[2];
				midiSendingSignature = (line.split(VALUE_SEPARATOR))[3];
			} catch (IndexOutOfBoundsException e) {
				// catch empty csv values
			}

			SetListItem item = (SetListItem) ctx.getBean("SetListItem");
			item.setName(name);
			item.setFilePath(filePath);
			item.setMidiListeningSignature(midiListeningSignature);
			item.setMidiSendingSignature(midiSendingSignature);

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

		FileWriter fileWriter = new FileWriter(persistenceFileName);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		for (SetListItem item : setList.getItems()) {
			log.debug("Save item: " + item.getName() + " " + item.getFilePath());
			String csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR,
					item.getName(), item.getFilePath(),
					item.getMidiListeningSignature(),
					item.getMidiSendingSignature());
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

	@Override
	public void setSetList(SetList setList) {
		this.setList = setList;
	}

}
