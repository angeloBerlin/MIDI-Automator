package com.midi_automator;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * This class handles the OS specific resource locations
 * 
 * @author aguelle
 * 
 */
public class Resources {

	static Logger log = Logger.getLogger(Resources.class.getName());

	private static Resources instance;

	private final String OPERATING_SYSTEM;
	private final String WORKING_DIRECTORY;
	private final String IMAGE_PATH;
	private final String PROPERTIES_PATH;
	private final String DEFAULT_FILE_LIST_PATH;

	/**
	 * Constructor
	 * 
	 * @param operatingSystem
	 *            The operating system
	 * @param workingDirectory
	 *            The working directory
	 */
	private Resources(String operatingSystem, String workingDirectory) {

		OPERATING_SYSTEM = operatingSystem;
		WORKING_DIRECTORY = workingDirectory;

		switch (OPERATING_SYSTEM) {
		case "MacOS":
			IMAGE_PATH = WORKING_DIRECTORY + "/images/";
			PROPERTIES_PATH = WORKING_DIRECTORY;
			DEFAULT_FILE_LIST_PATH = WORKING_DIRECTORY;
			break;
		case "Win":
			IMAGE_PATH = "";
			PROPERTIES_PATH = "";
			DEFAULT_FILE_LIST_PATH = "";
			break;
		default:
			IMAGE_PATH = "images" + File.separator;
			PROPERTIES_PATH = WORKING_DIRECTORY;
			DEFAULT_FILE_LIST_PATH = WORKING_DIRECTORY;
		}
	}

	/**
	 * Constructor
	 * 
	 * @param operatingSystem
	 *            The operating system
	 * @param workingDirectory
	 *            The working directory
	 */
	public static Resources getInstance(String operatingSystem,
			String workingDirectory) {

		if (instance == null) {
			instance = new Resources(operatingSystem, workingDirectory);
		}
		return instance;
	}

	public String getImagePath() {
		return IMAGE_PATH;
	}

	public String getPropertiesPath() {
		return PROPERTIES_PATH;
	}

	public String getDefaultFileListPath() {
		return DEFAULT_FILE_LIST_PATH;
	}

	public String getWorkingDirectory() {
		return WORKING_DIRECTORY;
	}

	/**
	 * Handles the relative working directory for the file list
	 * 
	 * @param path
	 *            The path from the file list
	 * @return The corrected relative path
	 */
	public String generateRelativeLoadingPath(String path) {
		String result = null;
		// initialize resources
		switch (OPERATING_SYSTEM) {
		case "MacOS":
			result = path;
			break;

		case "Win":
			result = path;
			break;

		default:
			result = path;
		}

		log.debug("Relative loading path: " + result);
		return result;
	}

}