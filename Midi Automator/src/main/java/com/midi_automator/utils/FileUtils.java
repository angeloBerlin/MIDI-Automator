package com.midi_automator.utils;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
	 * Opens a file with the given application
	 * 
	 * @param filePath
	 *            The path to the file to open
	 * @param programPath
	 *            The path to the program the file should be opened with
	 */
	public static void openFileFromPathWithProgram(String filePath,
			String programPath) throws IllegalArgumentException, IOException {

		String[] cmd = null;

		if (filePath == null) {
			filePath = "";
		}

		if (System.getProperty("os.name").equals("Mac OS X")) {
			cmd = new String[] { "open", "-a", programPath, filePath };
		}

		if (System.getProperty("os.name").contains("Windows")) {
			cmd = new String[] { programPath, filePath };
		}

		ShellRunner sheallRunner = new ShellRunner(cmd);
		sheallRunner.start();
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

			if (value == null) {
				value = "";
			}

			result = result + value + separator;
		}
		result = result.substring(0, result.length() - 1);
		log.debug("Built CSV line: \"" + result + "\"");

		return result;
	}

	/**
	 * Unzips a file to a given path
	 * 
	 * @param zipFile
	 *            The zip file
	 * @param unzipPath
	 *            The path to unzip the files to
	 * @throws IOException
	 */
	public static void unzipFile(ZipFile zipFile, String unzipPath)
			throws IOException {

		log.debug("Load file  to unzip: " + zipFile);
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

		while (enumeration.hasMoreElements()) {

			ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
			BufferedInputStream bis = new BufferedInputStream(
					zipFile.getInputStream(zipEntry));
			int size;
			byte[] buffer = new byte[2048];
			String unzippedFile = unzipPath + "/" + zipEntry.getName();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(unzippedFile), buffer.length);

			while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, size);
			}

			bos.flush();
			bos.close();
			bis.close();

			log.debug("Unizpped file: " + unzippedFile);
		}
	}

	/**
	 * Zips an array of files to a single archive.
	 * 
	 * @param inputFiles
	 *            An array of files
	 * @param zipFilePath
	 *            The path to the archive
	 * @throws IOException
	 * @throws ZipException
	 */
	public static void zipFiles(File[] inputFiles, String zipFilePath)
			throws IOException, ZipException {

		FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

		for (File file : inputFiles) {
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipOutputStream.putNextEntry(zipEntry);

			FileInputStream fileInputStream = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int bytesRead;

			while ((bytesRead = fileInputStream.read(buf)) > 0) {
				zipOutputStream.write(buf, 0, bytesRead);
			}

			fileInputStream.close();
			zipOutputStream.closeEntry();
			log.debug("Added file " + file.getAbsolutePath() + " to archive "
					+ zipFilePath);
		}

		zipOutputStream.close();
		fileOutputStream.close();

		log.debug("Crated zip file: " + zipFilePath);
	}

}
