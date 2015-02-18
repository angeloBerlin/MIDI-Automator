package de.tieffrequent.midi.automator.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	/**
	 * Opens a file with the application configured in the OS
	 * 
	 * @param filePath
	 *            The path to the file to open
	 */
	public static void openFileFromPath(String filePath)
			throws IllegalArgumentException, IOException {

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

		for (int i = 0; i < strings.length; i++) {
			String value = strings[i];

			if (value == null) {
				value = "";
			}

			result = result + value + separator;
		}
		result = result.substring(0, result.length() - 1);

		return result;
	}

}
