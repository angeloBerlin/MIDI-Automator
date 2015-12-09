package com.midi_automator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

/**
 * This class handles the OS specific resource locations
 * 
 * @author aguelle
 * 
 */
@Configuration
public class Resources {

	static Logger log = Logger.getLogger(Resources.class.getName());

	private final String OPERATING_SYSTEM;
	private final String WORKING_DIRECTORY;
	private final String IMAGE_PATH;
	private final String PROPERTIES_PATH;
	private final String DEFAULT_FILE_LIST_PATH;
	private final String PROJECT_PROPERTIES = "project.properties";
	private final String KEY_VERSION = "midiautomator.version";

	public Resources() {

		OPERATING_SYSTEM = Main.os;
		WORKING_DIRECTORY = Main.wd;

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

	/**
	 * Gets the version from the properties file
	 * 
	 * @return application version
	 */
	public String getVersion() {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(PROJECT_PROPERTIES);

		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(KEY_VERSION);
	}
}
