package com.midi_automator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.context.annotation.Configuration;

import com.midi_automator.utils.SystemUtils;

/**
 * This class handles the OS specific resource locations
 * 
 * @author aguelle
 * 
 */
@Configuration
public class Resources {

	static Logger log = Logger.getLogger(Resources.class.getName());

	private final String WORKING_DIRECTORY;
	private final String IMAGE_PATH;
	private final String PROPERTIES_PATH;
	private final String DEFAULT_FILE_LIST_PATH;
	private final String PROJECT_PROPERTIES = "project.properties";
	private final String KEY_VERSION = "midiautomator.version";
	private final String LOG_FILE_NAME = "MidiAutomator.log";
	private final String LOG_CONSOLE_APPENDER_NAME = "Console Appender";
	private final String LOG_FILE_APPENDER_NAME = "File Appender";

	public Resources() {

		WORKING_DIRECTORY = MidiAutomator.wd;

		if (System.getProperty("os.name").contains("Mac")) {
			IMAGE_PATH = WORKING_DIRECTORY + "/images";
			PROPERTIES_PATH = "/Users/Shared/Midi Automator";
			DEFAULT_FILE_LIST_PATH = "/Users/Shared/Midi Automator";
			configureLog4J("/Users/Shared/Midi Automator/" + LOG_FILE_NAME);
		} else if (System.getProperty("os.name").contains("Windows")) {
			IMAGE_PATH = "images";
			PROPERTIES_PATH = SystemUtils
					.replaceSystemVariables("%HOMEPATH%\\AppData\\Roaming\\Midi Automator");
			DEFAULT_FILE_LIST_PATH = SystemUtils
					.replaceSystemVariables("%HOMEPATH%\\AppData\\Roaming\\Midi Automator");
			configureLog4J(SystemUtils
					.replaceSystemVariables("%HOMEPATH%\\AppData\\Roaming\\Midi Automator\\")
					+ LOG_FILE_NAME);
		} else {
			IMAGE_PATH = "null";
			PROPERTIES_PATH = "null";
			DEFAULT_FILE_LIST_PATH = "null";
		}

		log.info("Working Driectory (-wd) set to: " + WORKING_DIRECTORY);
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

	/**
	 * Configures the Log4J properties
	 * 
	 * @param logFilePath
	 *            The log file path
	 */
	private void configureLog4J(String logFilePath) {

		// This is the root logger provided by log4j
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);

		// Define log pattern layout
		PatternLayout layout = new PatternLayout("[%-5p] %d %c - %m%n");

		// Add console appender to root logger
		if (rootLogger.getAppender(LOG_CONSOLE_APPENDER_NAME) == null) {
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			consoleAppender.setName(LOG_CONSOLE_APPENDER_NAME);
			rootLogger.addAppender(consoleAppender);
		}

		// Add file appender with layout and output log file name
		try {
			if (rootLogger.getAppender(LOG_FILE_APPENDER_NAME) == null) {
				FileAppender fileAppender = new FileAppender(layout,
						logFilePath);
				fileAppender.setAppend(false);
				fileAppender.setImmediateFlush(true);
				fileAppender.setName(LOG_FILE_APPENDER_NAME);
				rootLogger.addAppender(fileAppender);
			}
		} catch (IOException e) {
			System.out.println("Failed to add appender !!");
		}
	}

}
