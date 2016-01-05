package com.midi_automator.presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sound.midi.MidiUnavailableException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.Main;
import com.midi_automator.Resources;
import com.midi_automator.model.IModel;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.presenter.services.MidiMetronomService;
import com.midi_automator.presenter.services.MidiRemoteOpenService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.view.frames.MainFrame;

@Controller
public class Presenter {

	@Autowired
	private Resources resources;
	@Autowired
	private MidiAutomatorProperties properties;
	@Autowired
	private IModel model;

	@Autowired
	private MainFrame mainFrame;

	@Autowired
	private MidiService midiService;
	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private FileListService fileListService;
	@Autowired
	private MidiRemoteOpenService midiRemoteOpenService;
	@Autowired
	private MidiMetronomService midiMetronomService;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	private List<String> infoMessages;

	public Presenter() {

		infoMessages = new ArrayList<String>();
		Locale.setDefault(Main.locale);
	}

	/**
	 * Opens the main program frame.
	 */
	public MainFrame openMainFrame() {

		String fileName = model.getPersistenceFileName();

		String errMidoFileNotFound = String.format(
				Messages.MSG_FILE_LIST_NOT_FOUND, fileName);
		String errMidoFileNotReadable = String.format(
				Messages.MSG_FILE_LIST_NOT_READABLE, fileName);
		String errMidoFileIsTooBig = String.format(
				Messages.MSG_FILE_LIST_TOO_BIG, fileName);

		Messages.builtMessages.put(Messages.KEY_ERROR_MIDO_FILE_NOT_FOUND,
				errMidoFileNotFound);
		Messages.builtMessages.put(Messages.KEY_ERROR_ITEM_FILE_IO,
				errMidoFileNotReadable);
		Messages.builtMessages.put(Messages.KEY_ERROR_MIDO_FILE_TOO_BIG,
				errMidoFileIsTooBig);

		mainFrame.init();
		fileListService.reloadSetList();
		loadProperties();

		return mainFrame;
	}

	/**
	 * Loads the properties file.
	 * 
	 * @throws MidiUnavailableException
	 */
	public void loadProperties() {

		loadPropertiesFile();
		midiRemoteOpenService.loadProperties();
		midiMetronomService.loadProperties();
		midiNotificationService.loadProperties();
		fileListService.loadProperties();
		guiAutomationsService.loadProperties();
		mainFrame.reload();
	}

	/**
	 * Closes the application
	 */
	public void close() {
		midiService.unloadAllMidiDevices();
		guiAutomationsService.terminateAllGUIAutomators();
		fileListService.resetCurrentItem();
	}

	/**
	 * Returns if the application is in test mode
	 * 
	 * @return <TRUE> if the appplication is in development mode, <FALSE> if the
	 *         application is not in development mode
	 */
	public boolean isInTestMode() {
		return Main.test;
	}

	/**
	 * Sets an error message
	 * 
	 * @param message
	 *            The info message
	 */
	public void setInfoMessage(String message) {
		if (!infoMessages.contains(message)) {
			infoMessages.add(message);
		}
		mainFrame.setInfoText(messagesToString(infoMessages));
	}

	/**
	 * Removes the info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void removeInfoMessage(String message) {
		infoMessages.remove(message);

		if (mainFrame != null) {
			mainFrame.setInfoText(messagesToString(infoMessages));
		}
	}

	/**
	 * Transforms all messages to a HTML formatted String
	 * 
	 * @param messages
	 *            A list of messages
	 * @return A HTML formatted String
	 */
	private String messagesToString(List<String> messages) {
		String result = "";

		for (String message : messages) {
			result = result + message + "<br/>";
		}

		return result;
	}

	/**
	 * Loads the properties file.
	 */
	private void loadPropertiesFile() {

		try {

			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_FOUND));
			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE));

			properties.load();

		} catch (FileNotFoundException e) {

			String error = String.format(Messages.MSG_FILE_NOT_FOUND,
					properties.getPropertiesFilePath());
			Messages.builtMessages.put(
					Messages.KEY_ERROR_PROPERTIES_FILE_NOT_FOUND, error);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_FOUND));

		} catch (IOException e) {

			String error = String.format(Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					properties.getPropertiesFilePath());
			Messages.builtMessages.put(
					Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE, error);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE));
		}
	}

	/**
	 * Tries to store the properties file.
	 */
	public void storePropertiesFile() {

		try {
			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE));

			properties.store();

		} catch (IOException e) {

			String error = String.format(Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					properties.getPropertiesFilePath());
			Messages.builtMessages.put(
					Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE, error);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_READABLE));
		}
	}
}
