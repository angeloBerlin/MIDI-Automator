package com.midi_automator.presenter.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.MidiAutomatorProperties.GUIAutomationKey;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.Messages;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.view.frames.MainFrame;

/**
 * Handles all midi learning functions
 * 
 * @author aguelle
 *
 */
@Service
public class MidiLearnService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	private boolean midiLearning;
	private String midiLearningKey;

	public static final String KEY_MIDI_LEARN_PREVIOUS_BUTTON = "previous button";
	public static final String KEY_MIDI_LEARN_NEXT_BUTTON = "next button";
	public static final String KEY_MIDI_LEARN_FILE_LIST_ENTRY = "entry";
	public static final String KEY_MIDI_LEARN_AUTOMATION_TRIGGER = "GUI automation trigger";

	@Autowired
	private MidiAutomatorProperties properties;
	@Autowired
	private Model model;
	@Autowired
	private Presenter presenter;
	@Autowired
	protected MainFrame mainFrame;

	@Autowired
	private FileListService fileListService;
	@Autowired
	private InfoMessagesService infoMessagesService;

	public boolean isMidiLearning() {
		return midiLearning;
	}

	/**
	 * Sets the midi signature. The component will be implicitly taken from the
	 * learningComponent field
	 * 
	 * @param signature
	 *            The midi signature
	 */
	public void midiLearn(String signature) {

		if (midiLearningKey != null) {
			midiLearn(signature, midiLearningKey);
		}
	}

	/**
	 * Sets the midi signature for a given component
	 * 
	 * @param midiSignature
	 *            The midi signature
	 * @param key
	 *            The key of the learning component
	 */
	public void midiLearn(String midiSignature, String key) {

		if (key.equals(KEY_MIDI_LEARN_PREVIOUS_BUTTON)) {
			midiLearnPreviousButton(midiSignature);
		}

		if (key.equals(KEY_MIDI_LEARN_NEXT_BUTTON)) {
			midiLearnNextButton(midiSignature);
		}

		if (key.contains(KEY_MIDI_LEARN_FILE_LIST_ENTRY)) {
			midiLearnFileListEntry(midiSignature, getIndexFromMidiLearnKey(key));
		}

		if (key.contains(KEY_MIDI_LEARN_AUTOMATION_TRIGGER)) {
			mainFrame.setGUIAutomationTriggerMidiSignature(midiSignature);
		}
	}

	/**
	 * Unsets the midi signature for a given midi learn key
	 * 
	 * @param key
	 *            The key of the learning component
	 */
	public void unsetMidiSignature(String key) {
		midiLearn(null, key);

		if (key.contains(KEY_MIDI_LEARN_FILE_LIST_ENTRY)) {

			String entryName = fileListService
					.getEntryNameByIndex(getIndexFromMidiLearnKey(key));
			mainFrame.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					entryName));
		} else {
			mainFrame.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					key));
		}

	}

	/**
	 * Sets the application to midi learn mode
	 * 
	 * @param key
	 *            The key of the learning component
	 * @param index
	 *            An additional index to the key
	 */
	public void activateMidiLearn(String key, int index) {
		activateMidiLearn(key + " " + index);
	}

	/**
	 * Sets the application to midi learn mode
	 * 
	 * @param key
	 *            The key of the learning component
	 */
	public void activateMidiLearn(String key) {
		setMidiLearnMode(true);
		midiLearningKey = key;
	}

	/**
	 * Midi learns the previous button
	 * 
	 * @param midiSignature
	 *            The midi signature
	 */
	private void midiLearnPreviousButton(String midiSignature) {

		if (isMidiSignatureAlreadyStored(midiSignature)) {
			return;
		}

		properties.setProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE,
				midiSignature);

		presenter.storePropertiesFile();
		presenter.loadProperties();
	}

	/**
	 * Midi learns the next button
	 * 
	 * @param midiSignature
	 *            The midi signature
	 */
	private void midiLearnNextButton(String midiSignature) {

		if (isMidiSignatureAlreadyStored(midiSignature)) {
			return;
		}

		properties.setProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE,
				midiSignature);

		presenter.storePropertiesFile();
		presenter.loadProperties();
	}

	/**
	 * Midi learns a file list entry
	 * 
	 * @param midiSignature
	 *            The midi signature
	 * @param index
	 *            The index of the entry
	 */
	private void midiLearnFileListEntry(String midiSignature, int index) {

		if (isMidiSignatureAlreadyStored(midiSignature)) {
			return;
		}

		model.getSetList().getItems().get(index)
				.setMidiListeningSignature(midiSignature);
		fileListService.saveSetList();
	}

	/**
	 * Sets the midi learn mode
	 * 
	 * @param midiLearning
	 *            <TRUE> application is in midi learn mode, <FALSE> application
	 *            is not in midi learn mode
	 */
	public void setMidiLearnMode(boolean midiLearning) {
		this.midiLearning = midiLearning;

		if (midiLearning) {
			mainFrame.setInfoText(Messages.MSG_MIDI_LEARN_MODE);
		} else {
			midiLearningKey = null;
			mainFrame.midiLearnOff();
		}

		log.debug("Property midiLearn=" + midiLearning);
	}

	/**
	 * Gets the index number from a given key
	 * 
	 * @param key
	 *            The key
	 * @return The index number
	 */
	private int getIndexFromMidiLearnKey(String key) {
		String[] splitted = key.split(" ");
		String index = splitted[splitted.length - 1];
		return Integer.parseInt(index);
	}

	/**
	 * Unsets the midi signature for a given midi learn key
	 * 
	 * @param key
	 *            The key of the learning component
	 * @param index
	 *            An additional index for the key
	 */
	public void unsetMidiSignature(String key, int index) {
		unsetMidiSignature(key + " " + index);
	}

	/**
	 * Checks if the midi signature is already stored and displays a failure.
	 * 
	 * @param signature
	 *            The midi signature
	 * @return <TRUE> if it is already stored, <FALSE> if not
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private boolean isMidiSignatureAlreadyStored(String signature) {

		infoMessagesService.removeInfoMessage(Messages
				.get(Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE));

		if (signature == null) {
			signature = "";
			return false;
		}

		boolean found = false;

		if (properties != null) {
			@SuppressWarnings("unchecked")
			Enumeration<String> propertyNames = (Enumeration<String>) properties
					.propertyNames();

			while (propertyNames.hasMoreElements()) {
				String key = propertyNames.nextElement();
				String value = (String) properties.get(key);

				if (value.equals(signature)
						&& !key.contains(GUIAutomationKey.GUI_AUTOMATION_MIDI_SIGNATURE
								.toString())) {
					found = true;
				}
			}
		}

		if (model.getSetList().getMidiListeningSignatures().contains(signature)) {
			found = true;
		}

		if (signature.contains(MidiRemoteOpenService.OPEN_FILE_MIDI_SIGNATURE)) {
			found = true;
		}

		if (found == true) {
			String error = String.format(Messages.MSG_DUPLICATE_MIDI_SIGNATURE,
					signature);
			infoMessagesService.setInfoMessage(
					Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE, error);
		}

		return found;
	}
}
