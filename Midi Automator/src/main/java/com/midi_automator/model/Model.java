package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.midi_automator.Resources;
import com.midi_automator.utils.FileUtils;

public class Model implements IModel {

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

		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				persistenceFileName));

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {

			String name = "";
			String filePath = "";
			String midiSignature = "";

			try {
				name = (line.split(VALUE_SEPARATOR))[0];
				filePath = (line.split(VALUE_SEPARATOR))[1];
				midiSignature = (line.split(VALUE_SEPARATOR))[2];
			} catch (IndexOutOfBoundsException e) {
				// catch empty csv values
			}

			SetListItem item = (SetListItem) ctx.getBean("SetListItem");
			item.setName(name);
			item.setFilePath(filePath);
			item.setMidiSignature(midiSignature);

			setList.addItem(item);
		}
		bufferedReader.close();

		if (setList.getItems().size() > 128) {
			throw new TooManyEntriesException();
		}
	}

	@Override
	public void save() throws FileNotFoundException, IOException {

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
				persistenceFileName));

		for (SetListItem item : setList.getItems()) {

			String csvLine = FileUtils
					.buildCSVLineFromStrings(VALUE_SEPARATOR, item.getName(),
							item.getFilePath(), item.getMidiSignature());
			bufferedWriter.write(csvLine);
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
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
