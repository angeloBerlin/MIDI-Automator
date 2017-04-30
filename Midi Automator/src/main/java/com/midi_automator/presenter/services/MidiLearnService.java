package com.midi_automator.presenter.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.Messages;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.MidiAutomatorProperties.GUIAutomationKey;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.CommonUtils;
import com.midi_automator.view.tray.Tray;
import com.midi_automator.view.tray.menus.TrayPopupMenu;
import com.midi_automator.view.windows.MainFrame.ItemList;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationPanel;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

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
	public static final String KEY_MIDI_LEARN_ITEM_LIST_ENTRY = "entry";
	public static final String KEY_MIDI_LEARN_AUTOMATION_TRIGGER = "GUI automation trigger";
	public static final String KEY_MIDI_LEARN_HIDE_SHOW_MAIN_FRAME = "hiding program";

	@Autowired
	private MidiAutomatorProperties properties;
	@Autowired
	private Model model;
	@Autowired
	private Presenter presenter;
	@Autowired
	protected MainFrame mainFrame;
	@Autowired
	protected PreferencesDialog preferencesDialog;
	@Autowired
	private Tray tray;

	@Autowired
	private ItemListService fileListService;
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
	private void midiLearn(String midiSignature, String key) {

		if (isMidiSignatureAlreadyStored(midiSignature)) {
			return;
		}

		if (midiSignature != null) {
			infoMessagesService.setInfoMessage(String.format(
					Messages.MSG_MIDI_LEARN_SUCCESS, midiSignature));
		}

		String unindexdKey = getKeyFromIndexedMidiLearnKey(key);

		switch (getKeyFromIndexedMidiLearnKey(unindexdKey)) {
		case KEY_MIDI_LEARN_PREVIOUS_BUTTON:
			midiLearnPreviousButton(midiSignature);
			break;

		case KEY_MIDI_LEARN_NEXT_BUTTON:
			midiLearnNextButton(midiSignature);
			break;

		case KEY_MIDI_LEARN_ITEM_LIST_ENTRY:
			midiLearnFileListEntry(midiSignature,
					getIndexFromIndexedMidiLearnKey(key));
			break;

		case KEY_MIDI_LEARN_AUTOMATION_TRIGGER:
			preferencesDialog.setAutomationMidiSignature(midiSignature);
			break;

		case KEY_MIDI_LEARN_HIDE_SHOW_MAIN_FRAME:
			midiLearnHideMainFrame(midiSignature);
			break;
		}
	}

	/**
	 * Sets the application to midi learn mode
	 * 
	 * @param componentName
	 *            The name of the learning component
	 */
	public void activateMidiLearn(String componentName) {

		midiLearningKey = retrieveLearningKeyForComponentName(componentName);
		setMidiLearnMode(true);

	}

	/**
	 * Midi learns the signature for hiding the main frame
	 * 
	 * @param midiSignature
	 *            The midi signature
	 */
	private void midiLearnHideMainFrame(String midiSignature) {

		properties.setProperty(
				MidiAutomatorProperties.KEY_HIDE_PROGRAM_SIGNATURE,
				midiSignature);

		presenter.storePropertiesFile();
		presenter.loadProperties();
	}

	/**
	 * Midi learns the previous button
	 * 
	 * @param midiSignature
	 *            The midi signature
	 */
	private void midiLearnPreviousButton(String midiSignature) {

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
			infoMessagesService.clearInfoMessages();
			infoMessagesService.setInfoMessage(Messages.MSG_MIDI_LEARN_MODE_ON);
			mainFrame.midiLearnOn(midiLearningKey);
		} else {
			infoMessagesService
					.removeInfoMessage(Messages.MSG_MIDI_LEARN_MODE_ON);
			midiLearningKey = null;
			mainFrame.midiLearnOff();
		}

		log.debug("Property midiLearn=" + midiLearning);
	}

	/**
	 * Gets the index number from a given key
	 * 
	 * @param key
	 *            The indexed key
	 * @return The index number
	 */
	public int getIndexFromIndexedMidiLearnKey(String key) {
		String[] splitted = key.split(" ");
		String index = splitted[splitted.length - 1];
		return Integer.parseInt(index);
	}

	/**
	 * Gets the key from an indexed key
	 * 
	 * @param key
	 *            The indexed key
	 * @return The component key
	 */
	public String getKeyFromIndexedMidiLearnKey(String key) {
		String[] splitted = key.split(" ");

		String lastSplit = splitted[splitted.length - 1];
		String keyValue = "";

		if (CommonUtils.isInteger(lastSplit, 3)) {
			for (int i = 0; i < splitted.length - 1; i++) {
				keyValue = keyValue + splitted[i];

				if (i < splitted.length - 2) {
					keyValue = keyValue + " ";
				}
			}
		} else {
			keyValue = key;
		}

		return keyValue;
	}

	/**
	 * Unsets the midi signature for a given midi learn key
	 * 
	 * @param componentName
	 *            The name of the learning component
	 */
	public void midiUnlearn(String componentName) {

		String key = retrieveLearningKeyForComponentName(componentName);
		midiLearn(null, key);

		infoMessagesService.clearInfoMessages();

		if (key.contains(KEY_MIDI_LEARN_ITEM_LIST_ENTRY)) {

			String entryName = fileListService
					.getEntryNameByIndex(getIndexFromIndexedMidiLearnKey(key));
			infoMessagesService.setInfoMessage(String.format(
					Messages.MSG_MIDI_UNLEARNED, entryName));
		} else {
			infoMessagesService.setInfoMessage(String.format(
					Messages.MSG_MIDI_UNLEARNED, key));
		}
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

			Enumeration<?> propertyNames = (Enumeration<?>) properties
					.propertyNames();

			while (propertyNames.hasMoreElements()) {
				Object element = propertyNames.nextElement();

				String key = "";
				if (element instanceof String) {
					key = (String) element;
				}

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

		if (signature.contains(MidiExecuteService.OPEN_FILE_MIDI_SIGNATURE)) {
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

	/**
	 * Gets the learning key for the invoking component name
	 * 
	 * @param invokerName
	 *            The name of the invoked component
	 * @return The learning key, <NULL> if no key exists
	 */
	private String retrieveLearningKeyForComponentName(String invokerName) {

		switch (invokerName) {

		case MainFrame.NAME_PREV_BUTTON:
			return MidiLearnService.KEY_MIDI_LEARN_PREVIOUS_BUTTON;

		case MainFrame.NAME_NEXT_BUTTON:
			return MidiLearnService.KEY_MIDI_LEARN_NEXT_BUTTON;

		case ItemList.NAME:
			int selectedIndex = mainFrame.getItemList().getSelectedIndex();
			return MidiLearnService.KEY_MIDI_LEARN_ITEM_LIST_ENTRY + " "
					+ selectedIndex;
		case GUIAutomationTable.NAME:

			PreferencesDialog preferencesDialog = mainFrame
					.getPreferencesDialog();
			GUIAutomationPanel panel = preferencesDialog
					.getGuiAutomationPanel();
			GUIAutomationTable table = panel.getGUIAutomationsTable();

			int selectedRow = table.getSelectedRow();
			return MidiLearnService.KEY_MIDI_LEARN_AUTOMATION_TRIGGER + " "
					+ selectedRow;

		case TrayPopupMenu.NAME:
			return MidiLearnService.KEY_MIDI_LEARN_HIDE_SHOW_MAIN_FRAME;
		}

		return null;
	}

	/**
	 * Checks if a midi signature was learned for the given component name
	 * 
	 * @param componentName
	 *            The name of the component
	 * @return <TRUE> if it was learned, <FALSE> if it was not learned or
	 *         unlearned
	 */
	public boolean isMidiLearned(String componentName) {
		boolean isLearned = false;

		switch (componentName) {

		case MainFrame.NAME_PREV_BUTTON:
			String prevSignature = getPreviousButtonMidiListeningSignature();
			isLearned = isValidMidiSignature(prevSignature);
			break;

		case MainFrame.NAME_NEXT_BUTTON:
			String nextSignature = getNextButtonMidiListeningSignature();
			isLearned = isValidMidiSignature(nextSignature);
			break;

		case Tray.NAME:
			String hideSignature = getMainFrameHideMidiListeningSignature();
			isLearned = isValidMidiSignature(hideSignature);
			break;

		case ItemList.NAME:
			String selectedSignature = fileListService
					.getMidiFileListListeningSignature(mainFrame.getItemList()
							.getSelectedIndex());
			isLearned = isValidMidiSignature(selectedSignature);
			break;
		}

		return isLearned;
	}

	/**
	 * Checks if the given signature is not null, not empty or not set.
	 * 
	 * @param signature
	 *            The MID signature
	 * @return <TRUE>, if the signature is valid, <FALSE> if the signature is
	 *         not valid
	 */
	private boolean isValidMidiSignature(String signature) {

		if (signature != null
				&& !signature.equals(MidiAutomatorProperties.VALUE_NULL)
				&& !signature.equals("")) {
			return true;
		}

		return false;
	}

	/**
	 * Gets the midi listening signature for hiding the main frame
	 * 
	 * @return The midi signature
	 */
	public String getMainFrameHideMidiListeningSignature() {

		return properties
				.getProperty(MidiAutomatorProperties.KEY_HIDE_PROGRAM_SIGNATURE);
	}

	/**
	 * Gets the midi listening signature of the switch directions
	 * 
	 * @return The midi signature
	 */
	public String getPreviousButtonMidiListeningSignature() {

		return properties
				.getProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);
	}

	/**
	 * Gets the midi listening signature of the next switch directions
	 * 
	 * @return The midi signature
	 */
	public String getNextButtonMidiListeningSignature() {

		return properties
				.getProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE);

	}
}
