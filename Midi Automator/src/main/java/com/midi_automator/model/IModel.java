package com.midi_automator.model;

import java.util.List;

public interface IModel {

	/**
	 * Returns the names of the entries in the file list
	 * 
	 * @return The names of the list entries
	 */
	public List<String> getEntryNames();

	/**
	 * Returns the paths of the files
	 * 
	 * @return The paths of the files
	 */
	public List<String> getFilePaths();

	/**
	 * Returns the midi signatures used to open the files
	 * 
	 * @return The midi signatures
	 */
	public List<String> getMidiSignatures();

	/**
	 * Sets the midi signature for the given index
	 * 
	 * @param signature
	 *            The midi signature
	 * @param index
	 *            The file index for the signature
	 */
	public void setMidiSignature(String signature, int index);

	/**
	 * Gets the current chosen file
	 * 
	 * @return The index of the chosen file
	 */
	public int getCurrent();

	/**
	 * Sets the current chosen file
	 * 
	 * @param current
	 *            The index of the chosen file
	 */
	public void setCurrent(int current);

	/**
	 * Sets the file name of the persistence file
	 * 
	 * @param fileName
	 *            The file name
	 */
	public void setPersistenceFileName(String fileName);

	/**
	 * Changes the indexes of two items between each other
	 * 
	 * @param index1
	 *            first part of index pair to change
	 * @param index2
	 *            second part of index pair to change
	 */
	public void exchangeIndexes(int index1, int index2);

	/**
	 * Deletes the the entry with the given index
	 * 
	 * @param index
	 *            the index of the entry to delete
	 */
	public void deleteEntry(int index);

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
			String midiSignature);
}
