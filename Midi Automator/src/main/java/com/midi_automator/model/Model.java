package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.midi_automator.Resources;
import com.midi_automator.utils.FileUtils;

public class Model implements IModel {

	private List<String> fileMap;
	private int current;
	private static Model instance;
	private String persistenceFileName;
	private final String VALUE_SEPARATOR = ";";

	/**
	 * Private constructor for singleton pattern
	 * 
	 */
	private Model() {

	}

	/**
	 * Private constructor for singleton pattern
	 * 
	 * @param resources
	 *            The resources file
	 */
	private Model(Resources resources) {
		fileMap = new ArrayList<String>();
		current = -1;
		persistenceFileName = resources.getDefaultFileListPath()
				+ "file_list.mido";
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
	public List<String> getEntryNames() {

		List<String> result = new ArrayList<String>();
		for (String csvLine : fileMap) {
			result.add((csvLine.split(VALUE_SEPARATOR))[0]);
		}
		return result;
	}

	@Override
	public List<String> getFilePaths() {

		List<String> result = new ArrayList<String>();
		for (String csvLine : fileMap) {
			String[] split = csvLine.split(VALUE_SEPARATOR);

			if (split.length > 1) {
				result.add(split[1]);
			} else {
				result.add("");
			}
		}
		return result;
	}

	@Override
	public List<String> getMidiSignatures() {

		List<String> result = new ArrayList<String>();
		for (String csvLine : fileMap) {
			try {
				result.add((csvLine.split(VALUE_SEPARATOR))[2]);
			} catch (ArrayIndexOutOfBoundsException e) {
				result.add(null);
			}
		}
		return result;
	}

	@Override
	public void setMidiSignature(String signature, int index) {

		String csvLine = fileMap.get(index);
		String[] split = csvLine.split(";");
		csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR, split[0],
				split[1], signature);
		fileMap.set(index, csvLine);
	}

	@Override
	public int getCurrent() {
		return current;
	}

	@Override
	public void setCurrent(int current) {
		this.current = current;
	}

	@Override
	public void load() throws FileNotFoundException, IOException,
			TooManyEntriesException {

		fileMap.clear();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				persistenceFileName));

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			fileMap.add(line);
		}
		bufferedReader.close();

		if (fileMap.size() > 128) {
			fileMap.clear();
			throw new TooManyEntriesException();
		}
	}

	@Override
	public void save() throws FileNotFoundException, IOException {

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
				persistenceFileName));

		for (String csvLine : fileMap) {
			bufferedWriter.write(csvLine);
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
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
	public void exchangeIndexes(int index1, int index2) {

		try {
			String line1 = fileMap.get(index1);
			String line2 = fileMap.get(index2);

			fileMap.set(index2, line1);
			fileMap.set(index1, line2);

		} catch (IndexOutOfBoundsException e) {

		}
	}

	@Override
	public void deleteEntry(int index) {

		try {
			fileMap.remove(index);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	@Override
	public void setEntry(Integer index, String entryName, String filePath,
			String midiSignature) {

		String csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR,
				entryName, filePath, midiSignature);

		if (index == null) {
			fileMap.add(csvLine);
		} else {
			fileMap.set(index, csvLine);
		}
	}
}
