package com.midi_automator.presenter.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.frames.PreferencesFrame;

/**
 * Handles all key learning functions
 * 
 * @author aguelle
 *
 */
@Service
public class KeyLearnService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	private boolean keyLearning;
	private boolean newLearning;
	private String keyLearningKey;
	private String keysCache = "";

	public static final String KEY_KEY_LEARN_AUTOMATION = "Key automation";

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	protected MainFrame mainFrame;
	@Autowired
	protected PreferencesFrame preferencesFrame;

	/**
	 * Returns if the the key learning mode is still active.
	 * 
	 * @return <TRUE> still key learning, <FALSE> not key learning any longer
	 */
	public boolean isKeyLearning() {
		return keyLearning;
	}

	/**
	 * Returns if a new learning process was started.
	 * 
	 * @return <TRUE> new learning, <FALSE> still learning
	 */
	public boolean isNewLearning() {
		return newLearning;
	}

	public void setNewLearning(boolean newLearning) {
		this.newLearning = newLearning;
	}

	/**
	 * Sets the key signature.
	 * 
	 * @param signature
	 *            The midi signature
	 */
	public void keyLearn(int keyCode) {

		if (keyLearningKey != null) {
			keyLearn(keyCode, keyLearningKey);
		}
	}

	/**
	 * Sets the key code for a given component
	 * 
	 * @param keyCode
	 *            The key code
	 * @param key
	 *            The key of the learning component
	 */
	public void keyLearn(int keyCode, String key) {

		if (key.contains(KEY_KEY_LEARN_AUTOMATION)) {

			if (newLearning) {
				GUIAutomationConfigurationTable table = preferencesFrame
						.getGuiAutomationPanel().getGUIAutomationsTable();
				keysCache = table.getKeysOfSelectedRow();
				preferencesFrame.deleteKeyCodeFromSelectedAutomation();
				newLearning = false;
			}

			preferencesFrame.addKeyCodeToSelectedAutomation(keyCode);
		}
	}

	/**
	 * Deletes learned key codes
	 * 
	 * @param key
	 *            The key of the learning component
	 */
	public void deleteKeyCodes(String key) {

		if (key.contains(KEY_KEY_LEARN_AUTOMATION)) {
			preferencesFrame.deleteKeyCodeFromSelectedAutomation();
		}
	}

	/**
	 * Sets the application to key learn mode
	 * 
	 * @param key
	 *            The key of the learning component
	 * @param index
	 *            An additional index to the key
	 */
	public void activateKeyLearn(String key, int index) {
		activateKeyLearn(key + " " + index);
	}

	/**
	 * Sets the application to key learn mode
	 * 
	 * @param key
	 *            The key of the learning component
	 */
	public void activateKeyLearn(String key) {
		setKeyLearnMode(true);
		keyLearningKey = key;
		newLearning = true;
	}

	/**
	 * Sets the application to key learn mode
	 * 
	 */
	public void deActivateKeyLearn() {
		setKeyLearnMode(false);
		newLearning = false;
	}

	/**
	 * Cancels key learning and rolls back the key cache
	 * 
	 */
	public void cancelKeyLearn() {
		setKeyLearnMode(false);
		GUIAutomationConfigurationTable table = preferencesFrame
				.getGuiAutomationPanel().getGUIAutomationsTable();
		table.setKeysOfSelectedRow(keysCache);
		newLearning = false;
	}

	/**
	 * Sets the key learn mode
	 * 
	 * @param learning
	 *            <TRUE> application is in key learn mode, <FALSE> application
	 *            is not in key learn mode
	 */
	private void setKeyLearnMode(boolean learning) {
		keyLearning = learning;

		if (!learning) {
			keyLearningKey = null;
			mainFrame.keyLearnOff();
		}

		log.debug("Property keyLearn=" + learning);
	}
}
