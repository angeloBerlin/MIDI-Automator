package com.midi_automator.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileUtils {

	static Logger log = Logger.getLogger(FileUtils.class.getName());

	/**
	 * Opens a file with the application configured in the OS
	 * 
	 * @param filePath
	 *            The path to the file to open
	 */
	public static void openFileFromPath(String filePath)
			throws IllegalArgumentException, IOException {

		if (filePath == null) {
			filePath = "";
		}

		File file = new File(filePath);
		Desktop.getDesktop().open(file);
	}

	/**
	 * Takes a list of file paths and returns the list with file names only
	 * 
	 * @param filePaths
	 *            The list with file paths
	 * @return The list with file names
	 */
	public static List<String> getFileNames(List<String> filePaths) {

		List<String> result = new ArrayList<String>();

		for (String filePath : filePaths) {
			result.add(getFileName(filePath));
		}
		return result;
	}

	/**
	 * Returns the file name of the given file path
	 * 
	 * @param filePath
	 *            The path of the file
	 * @return The file name
	 */
	public static String getFileName(String filePath) {
		String result;
		result = filePath.substring(filePath.lastIndexOf('/') + 1);
		return result;
	}

	/**
	 * Builds a CSV line from multiple string parameters
	 * 
	 * @param separator
	 *            The separator between array values
	 * @param strings
	 *            The values as string parameters
	 * @return a CSV line
	 */
	public static String buildCSVLineFromStrings(String separator,
			String... strings) {
		log.debug("Build CSV from String");
		return buildCSVLineFromArray(separator, strings);
	}

	/**
	 * Builds a CSV line from an array
	 * 
	 * @param separator
	 *            The separator between array values
	 * @param strings
	 *            The values as string parameters
	 * @return a CSV line
	 */
	public static String buildCSVLineFromArray(String separator,
			String[] strings) {
		String result = "";

		log.debug("Begin building CSV line...");
		for (int i = 0; i < strings.length; i++) {
			String value = strings[i];

			if (value == null || value.equals(" ")) {
				value = "";
			}

			result = result + value + separator;
		}
		result = result.substring(0, result.length() - 1);
		log.debug("Built CSV line: \"" + result + "\"");

		return result;
	}

}
