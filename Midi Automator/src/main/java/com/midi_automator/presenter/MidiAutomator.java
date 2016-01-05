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
import com.midi_automator.presenter.services.MidiRemoteOpenService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.view.frames.MainFrame;

@Controller
public class MidiAutomator {

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

	private List<String> infoMessages;

	public MidiAutomator() {

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
		reloadProperties();

		return mainFrame;
	}

	/**
	 * Loads the properties file.
	 * 
	 * @throws MidiUnavailableException
	 */
	public void reloadProperties() {

		loadPropertiesFile();

		// MIDI IN Remote
		loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);

		// MIDI OUT Remote
		loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);

		// MIDI IN Metronom
		loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE);

		// MIDI OUT Switch Notifier
		loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);

		// MIDI OUT Switch Items
		loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);

		// PREV
		loadSwitchCommandProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);

		// NEXT
		loadSwitchCommandProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE);

		// GUI automation
		guiAutomationsService.loadGUIAutomationsProperties();

		// MIDI IN Automation Triggers
		for (int i = 0; i < guiAutomationsService.getGuiAutomations().length; i++) {
			loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
					+ "_" + i);
		}

		mainFrame.reload();
	}

	/**
	 * Loads the switch command properties for PREVIOUS and NEXT.
	 * 
	 * @param propertyKey
	 *            The property key
	 */
	private void loadSwitchCommandProperty(String propertyKey) {

		String propertyValue = (String) properties.get(propertyKey);

		if (propertyValue != null) {
			String buttonName = "";

			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE)) {
				buttonName = MainFrame.NAME_PREV_BUTTON;
			}

			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE)) {
				buttonName = MainFrame.NAME_NEXT_BUTTON;
			}

			if (buttonName != "") {
				mainFrame.setButtonTooltip(propertyValue,
						MainFrame.NAME_NEXT_BUTTON);
			}
		}
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
	 * Loads a midi device property by opening and connecting the configured
	 * midi devices.
	 * 
	 * 
	 * @param propertyKey
	 *            The property key for the midi device
	 */
	private void loadMidiDeviceProperty(String propertyKey) {

		String propertyValue = properties.getProperty(propertyKey);
		midiService.loadMidiDeviceByFunctionKey(propertyKey, propertyValue);
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
	public void loadPropertiesFile() {

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
