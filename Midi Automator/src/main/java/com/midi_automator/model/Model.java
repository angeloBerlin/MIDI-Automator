package com.midi_automator.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.midi_automator.Messages;
import com.midi_automator.MidiAutomator;
import com.midi_automator.utils.FileUtils;

public class Model {

	private MidiAutomator application;
	private List<String> fileMap;
	private int current;
	private String errFileNotFound;
	private String errFileNotReadable;
	private String errFileTooBig;

	private static Model instance;

	private String persistenceFileName;
	private final String VALUE_SEPARATOR = ";";

	/**
	 * Private constructor for singleton pattern
	 * 
	 * @param application
	 *            The main application
	 */
	private Model(MidiAutomator application) {
		fileMap = new ArrayList<String>();
		current = -1;
		this.application = application;
		persistenceFileName = application.getResources()
				.getDefaultFileListPath() + "file_list.mido";
	}

	/**
	 * Gets the unique instance of the model
	 * 
	 * @param application
	 *            The main application
	 * @return Singleton instance
	 */
	public static Model getInstance(MidiAutomator application) {

		if (instance == null) {
			instance = new Model(application);
		}

		return instance;
	}

	/**
	 * Returns the names of the entries in the file list
	 * 
	 * @return The names of the list entries
	 */
	public List<String> getEntryNames() {

		load();
		List<String> result = new ArrayList<String>();
		for (String csvLine : fileMap) {
			result.add((csvLine.split(VALUE_SEPARATOR))[0]);
		}
		return result;
	}

	/**
	 * Returns the paths of the files
	 * 
	 * @return The paths of the files
	 */
	public List<String> getFilePaths() {

		load();
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

	/**
	 * Returns the midi signatures used to open the files
	 * 
	 * @return The midi signatures
	 */
	public List<String> getMidiSignatures() {

		load();
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

	/**
	 * Sets the midi signature for the given index
	 * 
	 * @param signature
	 *            The midi signature
	 * @param index
	 *            The file index for the signature
	 */
	public void setMidiSignature(String signature, int index) {

		load();
		String csvLine = fileMap.get(index);
		String[] split = csvLine.split(";");
		csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR, split[0],
				split[1], signature);
		fileMap.set(index, csvLine);
		save();
	}

	/**
	 * Gets the current chosen file
	 * 
	 * @return The index of the chosen file
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Sets the current chosen file
	 * 
	 * @param current
	 *            The index of the chosen file
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	/**
	 * Loads the persistent file
	 */
	private void load() {

		fileMap.clear();
		try {

			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					persistenceFileName));

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				fileMap.add(line);
			}
			bufferedReader.close();

			application.removeInfoMessage(errFileNotFound);
			application.removeInfoMessage(errFileNotReadable);
			application.removeInfoMessage(errFileTooBig);

			if (fileMap.size() > 128) {
				fileMap.clear();
				errFileTooBig = String.format(Messages.MSG_FILE_LIST_TOO_BIG,
						persistenceFileName);
				application.setInfoMessage(errFileTooBig);
			}

		} catch (FileNotFoundException e) {

			errFileNotFound = String.format(Messages.MSG_FILE_LIST_NOT_FOUND,
					persistenceFileName);
			application.setInfoMessage(errFileNotFound);

		} catch (IOException e) {

			errFileNotReadable = String.format(
					Messages.MSG_FILE_LIST_NOT_READABLE, persistenceFileName);
			application.setInfoMessage(errFileNotReadable);
		}

	}

	/**
	 * Saves to the persistent file
	 */
	private void save() {

		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					persistenceFileName));

			for (String csvLine : fileMap) {
				bufferedWriter.write(csvLine);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();

			application.removeInfoMessage(errFileNotFound);
			application.removeInfoMessage(errFileNotReadable);

		} catch (FileNotFoundException e) {
			application.setInfoMessage(errFileNotFound);
		} catch (IOException e) {
			application.setInfoMessage(errFileNotReadable);
		}
	}

	/**
	 * Sets the file name of the persistence file
	 * 
	 * @param fileName
	 *            The file name
	 */
	public void setPersistenceFileName(String fileName) {

		if (fileName != null) {
			persistenceFileName = fileName;
		}
	}

	/**
	 * Changes the indexes of two items between each other
	 * 
	 * @param index1
	 *            first part of index pair to change
	 * @param index2
	 *            second part of index pair to change
	 */
	public void exchangeIndexes(int index1, int index2) {

		try {
			String line1 = fileMap.get(index1);
			String line2 = fileMap.get(index2);

			fileMap.set(index2, line1);
			fileMap.set(index1, line2);
			save();
		} catch (IndexOutOfBoundsException e) {

		}
	}

	/**
	 * Deletes the the entry with the given index
	 * 
	 * @param index
	 *            the index of the entry to delete
	 */
	public void deleteEntry(int index) {

		try {
			fileMap.remove(index);
			save();
		} catch (IndexOutOfBoundsException e) {

		}
	}

	/**
	 * Sets the attributes of an entry
	 * 
	 * @param index
	 *            the index of the entry, if <NULL> entry will be added with the
	 *            next available index
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param midiSignature
	 *            the midi signature
	 */
	public void setEntry(Integer index, String entryName, String filePath,
			String midiSignature) {
		String csvLine = FileUtils.buildCSVLineFromStrings(VALUE_SEPARATOR,
				entryName, filePath, midiSignature);

		if (index == null) {
			fileMap.add(csvLine);
		} else {
			fileMap.set(index, csvLine);
		}
		save();
	}
}
