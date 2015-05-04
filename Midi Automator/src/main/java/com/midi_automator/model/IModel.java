package com.midi_automator.model;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IModel {

	/**
	 * Sets the file name of the persistence file
	 * 
	 * @param fileName
	 *            The file name
	 */
	public void setPersistenceFileName(String fileName);

	/**
	 * Gets the file name of the model
	 * 
	 * @return The name of the persisting file
	 */
	public String getPersistenceFileName();

	/**
	 * Gets the set list
	 * 
	 * @return a SetList object
	 */
	public SetList getSetList();

	/**
	 * Sets the set list
	 * 
	 * @param setList
	 *            a SetList object
	 */
	public void setSetList(SetList setList);

	/**
	 * Loads the persistent file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TooManyEntriesException
	 */
	public void load() throws FileNotFoundException, IOException,
			TooManyEntriesException;

	/**
	 * Saves to the persistent file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void save() throws FileNotFoundException, IOException;
}
