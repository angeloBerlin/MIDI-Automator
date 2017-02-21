package com.midi_automator.presenter.services;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;

/**
 * Handles all helping functions for the presenter.
 * 
 * @author aguelle
 *
 */
@Service
public class PresenterService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MidiAutomatorProperties properties;

	/**
	 * Gets the last directory of the file chooser
	 * 
	 * @return The directory path
	 */
	public String getLastFileChooserDirectory() {
		return (String) properties
				.get(MidiAutomatorProperties.KEY_LAST_FILE_CHOOSER_DIR);
	}

	/**
	 * Gets the last directory of the program chooser
	 * 
	 * @return The directory path
	 */
	public String getLastProgramChooserDirectory() {
		return (String) properties
				.get(MidiAutomatorProperties.KEY_LAST_PROGRAM_CHOOSER_DIR);
	}

	/**
	 * Gets the last directory of the screenshot chooser
	 * 
	 * @return The directory path
	 */
	public String getLastScreenshotChooserDirectory() {
		return (String) properties
				.get(MidiAutomatorProperties.KEY_LAST_SCREENSHOT_CHOOSER_DIR);
	}

	/**
	 * Gets the flag if the program shall be minimized on closed.
	 * 
	 * @return <TRUE> the program shall be minimized on close, <FALSE> the
	 *         program shall be closed.
	 */
	public boolean isMinimizeOnClose() {

		String minimizeOnClose = (String) properties
				.get(MidiAutomatorProperties.KEY_MINIMIZE_ON_CLOSE);

		if (minimizeOnClose != null) {
			if (minimizeOnClose.equals("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the last chosen directory for the file chooser
	 * 
	 * @param dir
	 *            The last directory
	 */
	public void setLastFileChooserDirectory(String dir) {
		properties.setProperty(
				MidiAutomatorProperties.KEY_LAST_FILE_CHOOSER_DIR, dir);
		try {
			properties.store();
		} catch (IOException e) {
			log.error("Storing last file chooser directory failed.", e);
		}
	}

	/**
	 * Sets the last chosen directory for the program chooser
	 * 
	 * @param dir
	 *            The last directory
	 */
	public void setLastProgramChooserDirectory(String dir) {
		properties.setProperty(
				MidiAutomatorProperties.KEY_LAST_PROGRAM_CHOOSER_DIR, dir);
		try {
			properties.store();
		} catch (IOException e) {
			log.error("Storing last program chooser directory failed.", e);
		}
	}

	/**
	 * Sets the last chosen directory for the screenshot chooser
	 * 
	 * @param dir
	 *            The last directory
	 */
	public void setLastScreenshotChooserDirectory(String dir) {
		properties.setProperty(
				MidiAutomatorProperties.KEY_LAST_SCREENSHOT_CHOOSER_DIR, dir);
		try {
			properties.store();
		} catch (IOException e) {
			log.error("Storing last screenshot chooser directory failed.", e);
		}
	}

	/**
	 * Sets the flag if the program shall be minimized on closed.
	 * 
	 * @param minimize
	 *            <TRUE> the program shall be minimized on close, <FALSE> the
	 *            program shall be closed.
	 */
	public void setMinimizeOnClose(boolean minimize) {
		properties.setProperty(MidiAutomatorProperties.KEY_MINIMIZE_ON_CLOSE,
				minimize);
		try {
			properties.store();
		} catch (IOException e) {
			log.error("Storing 'minimize on close' failed.", e);
		}
	}
}
