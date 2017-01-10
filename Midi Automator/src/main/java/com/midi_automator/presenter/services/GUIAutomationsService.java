package com.midi_automator.presenter.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.sikuli.script.Observing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.guiautomator.GUIAutomator;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.MidiAutomatorProperties.GUIAutomationKey;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.CommonUtils;
import com.midi_automator.utils.MidiUtils;

/**
 * Handles all GUI automations.
 * 
 * @author aguelle
 *
 */
@Service
public class GUIAutomationsService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	private Presenter presenter;

	@Autowired
	private MidiService midiService;

	private GUIAutomation[] guiAutomations;
	private float minSimilarity;
	private List<GUIAutomator> guiAutomators = new ArrayList<GUIAutomator>();
	public static final String PIDSEPARATOR = "PID: ";

	/**
	 * Loads all GUI automations from the properties.
	 */
	public void loadProperties() {

		// initiate array with GUI automations
		Set<Entry<Object, Object>> guiAutomationProperties = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_IMAGE.toString());

		guiAutomations = new GUIAutomation[guiAutomationProperties.size()];

		for (int i = 0; i < guiAutomations.length; i++) {
			guiAutomations[i] = new GUIAutomation();
		}

		loadAutomationImageProperties();
		loadAutomationTypeProperties();
		loadAutomationKeyCodesProperties();
		loadAutomationTriggerProperties();
		loadAutomationFocusedProgramProperties();
		loadAutomationMinDelayProperties();
		loadAutomationTimeoutProperties();
		loadAutomationMidiSignatureProperties();
		loadAutomationMinSimilarityProperty();
		loadAutomationScanRateProperties();
		loadAutomationIsMovableProperites();

		stopGUIAutomations();
		startGuiAutomations();
	}

	/**
	 * Starts all GUI automations
	 */
	private void startGuiAutomations() {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < guiAutomations.length; i++) {

					GUIAutomator guiAutomator = ctx.getBean("GUIAutomator",
							GUIAutomator.class);
					guiAutomator.init(minSimilarity);
					guiAutomator.setName("GUIAutomator " + i);
					guiAutomator.setGUIAutomation(guiAutomations[i]);
					guiAutomators.add(guiAutomator);
					guiAutomator.start();
				}
				return null;
			}
		};
		worker.execute();
	}

	/**
	 * Loads all key codes.
	 */
	private void loadAutomationKeyCodesProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_KEY_CODES.toString());
		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			String keyCodesString = (String) property.getValue();

			if (keyCodesString.equals(MidiAutomatorProperties.VALUE_NULL)) {
				keyCodesString = null;
			} else {
				int[] keyCodes = CommonUtils
						.stringArrayToIntArray(keyCodesString
								.split(MidiAutomatorProperties.KEY_CODES_DELIMITER));
				guiAutomations[index].setKeyCodes(keyCodes);
			}
		}
	}

	/**
	 * Loads all midi signatures.
	 */
	private void loadAutomationMidiSignatureProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_MIDI_SIGNATURE
						.toString());
		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			String value = (String) property.getValue();

			if (value.equals(MidiAutomatorProperties.VALUE_NULL)) {
				value = null;
			}

			guiAutomations[index].setMidiSignature(value);
		}
	}

	/**
	 * Loads all movable options.
	 */
	private void loadAutomationIsMovableProperites() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_IS_MOVABLE.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			boolean isMovable = Boolean.valueOf((String) property.getValue());
			guiAutomations[index].setMovable(isMovable);

		}
	}

	/**
	 * Loads all minimum similarities.
	 */
	private void loadAutomationMinSimilarityProperty() {
		String strMinSimilarity = properties
				.getProperty(GUIAutomationKey.GUI_AUTOMATION_MIN_SIMILARITY
						.toString());

		if (strMinSimilarity != null) {
			strMinSimilarity = strMinSimilarity.replace(",", ".");
			minSimilarity = Float.parseFloat(strMinSimilarity);
		}
	}

	/**
	 * Loads all scan rates.
	 */
	private void loadAutomationScanRateProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_SCAN_RATE.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			float value = Float.valueOf((String) property.getValue());
			guiAutomations[index].setScanRate(value);

		}
	}

	/**
	 * Loads all time outs.
	 */
	private void loadAutomationTimeoutProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_TIMEOUT.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			long timeout = Long.valueOf((String) property.getValue());
			guiAutomations[index].setTimeout(timeout);

		}
	}

	/**
	 * Loads all minimum delays.
	 */
	private void loadAutomationMinDelayProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_MIN_DELAY.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			long minDelay = Long.valueOf((String) property.getValue());
			guiAutomations[index].setMinDelay(minDelay);

		}
	}

	/**
	 * Loads all triggers.
	 */
	private void loadAutomationTriggerProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_TRIGGER.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			String trigger = (String) property.getValue();
			guiAutomations[index].setTrigger(trigger);

			if (trigger.contains(GUIAutomation.TRIGGER_MIDI)) {

				String midiDeviceKey = MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
						+ "_" + index;

				String midiDeviceName = trigger.replace(
						GUIAutomation.TRIGGER_MIDI, "");

				midiService.loadMidiDeviceByFunctionKey(midiDeviceKey,
						midiDeviceName);
			}

		}
	}

	/**
	 * Loads all focused programs.
	 */
	private void loadAutomationFocusedProgramProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_FOCUS.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			guiAutomations[index].setFocusedProgram((String) property
					.getValue());
		}
	}

	/**
	 * Loads all types.
	 */
	private void loadAutomationTypeProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_TYPE.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			guiAutomations[index].setType((String) property.getValue());

		}
	}

	/**
	 * Loads all images.
	 */
	private void loadAutomationImageProperties() {

		Set<Entry<Object, Object>> propertiesSet = properties
				.entrySet(GUIAutomationKey.GUI_AUTOMATION_IMAGE.toString());

		for (Entry<Object, Object> property : propertiesSet) {

			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			guiAutomations[index].setImagePath((String) property.getValue());

		}
	}

	/**
	 * Terminates all GUI automations
	 */
	public void stopGUIAutomations() {

		for (int i = 0; i < guiAutomators.size(); i++) {
			GUIAutomator guiAutomator = guiAutomators.get(i);
			guiAutomator.setActive(false);
			guiAutomator.terminate();

			log.debug("Terminate GUI automation: "
					+ guiAutomator.getGuiAutomation());
		}
		guiAutomators.clear();
		Observing.cleanUp();
	}

	/**
	 * Activates all GUI automations that listen to the given message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void activateAutomationsByMidiMessage(MidiMessage message) {

		String signature = MidiUtils.messageToString(message);

		// activate midi automations
		for (GUIAutomator guiAutomator : guiAutomators) {
			guiAutomator.activateMidiAutomations(signature);
		}

	}

	/**
	 * Sets all GUI automations.
	 * 
	 * @param guiAutomations
	 *            The GUIAutomations to store
	 */
	public void saveGUIAutomations(GUIAutomation[] guiAutomations,
			float minSimilarity) {

		removeGUIAutomations();

		saveProperty(GUIAutomationKey.GUI_AUTOMATION_MIN_SIMILARITY.toString(),
				String.format("%.2f", (float) minSimilarity));

		this.guiAutomations = guiAutomations;

		for (int index = 0; index < guiAutomations.length; index++) {

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_IMAGE.toString(), index,
					guiAutomations[index].getImagePath());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_TYPE.toString(), index,
					guiAutomations[index].getType());

			String keyCodesString = CommonUtils.intArrayToString(
					guiAutomations[index].getKeyCodes(),
					MidiAutomatorProperties.KEY_CODES_DELIMITER);

			if (keyCodesString == null) {
				keyCodesString = MidiAutomatorProperties.VALUE_NULL;
			}

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_KEY_CODES.toString(),
					index, keyCodesString);

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_TRIGGER.toString(), index,
					guiAutomations[index].getTrigger());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_FOCUS.toString(), index,
					guiAutomations[index].getFocusedProgram());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_MIN_DELAY.toString(),
					index, guiAutomations[index].getMinDelay());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_TIMEOUT.toString(), index,
					guiAutomations[index].getTimeout());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_MIDI_SIGNATURE.toString(),
					index, guiAutomations[index].getMidiSignature());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_SCAN_RATE.toString(),
					index, guiAutomations[index].getScanRate());

			saveAutomationProperty(
					GUIAutomationKey.GUI_AUTOMATION_IS_MOVABLE.toString(),
					index, guiAutomations[index].isMovable());
		}
	}

	/**
	 * Saves an automation property to the the properties file.
	 * 
	 * @param key
	 *            The key of the property
	 * @param index
	 *            The index of the automation
	 * @param value
	 *            The value of the property
	 */
	private void saveAutomationProperty(String key, int index, Object value) {

		String strValue = String.valueOf(value);

		if (strValue.equals("null") || strValue.equals("")) {
			strValue = MidiAutomatorProperties.VALUE_NULL;
		}

		properties.setProperty(key + MidiAutomatorProperties.INDEX_SEPARATOR
				+ index, strValue);
		presenter.storePropertiesFile();
	}

	/**
	 * Saves a common property to the properties file.
	 * 
	 * @param key
	 *            The key of the property
	 * 
	 * @param value
	 *            The value of the property
	 */
	private void saveProperty(String key, Object value) {

		properties.setProperty(key, value);
		presenter.storePropertiesFile();
	}

	/**
	 * Removes all GUI automations.
	 */
	public void removeGUIAutomations() {

		guiAutomations = null;

		for (GUIAutomationKey key : GUIAutomationKey.values()) {
			properties.removeKeys(key.toString());
		}

		presenter.storePropertiesFile();
	}

	/**
	 * Activate per change triggered automations
	 */
	public void activateAllOncePerChangeAutomations() {
		for (GUIAutomator guiAutomator : guiAutomators) {
			guiAutomator.activateOncePerChangeAutomations();
		}
	}

	/**
	 * De-/Activates the GUI automators.
	 * 
	 * @param active
	 *            <TRUE> activate GUI automation, <FALSE> deactivate GUI
	 *            automation
	 */
	public void setGUIAutomatorsToActive(boolean active) {
		for (GUIAutomator guiAutomator : guiAutomators) {
			guiAutomator.setActive(active);
		}
	}

	/**
	 * Gets all open programs.
	 * 
	 * @return A unique of open programs
	 */
	public List<String> getOpenPrograms() {

		HashSet<String> programs = new HashSet<String>();

		try {

			Process process = null;

			if (System.getProperty("os.name").contains("Mac")) {
				process = Runtime.getRuntime().exec("ps -few");
			}

			if (System.getProperty("os.name").contains("Windows")) {
				process = Runtime
						.getRuntime()
						.exec(System.getenv("windir")
								+ "\\system32\\"
								+ "tasklist.exe /nh /fo CSV /v /fi \"status eq running\"");
			}

			BufferedReader input = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			String line;
			String program = null;
			while ((line = input.readLine()) != null) {

				if (System.getProperty("os.name").contains("Mac")) {
					program = parseMacProcessLine(line);
				}

				if (System.getProperty("os.name").contains("Windows")) {
					program = parseWindowsProcessLine(line);
				}

				if (program != null) {
					programs.add(program);
				}
			}
			input.close();
		} catch (IOException e) {
			log.error("Failed to run process.", e);
		}

		// sort alphabetically
		ArrayList<String> programList = new ArrayList<String>(programs);
		Collections.sort(programList,
				(String program1, String program2) -> program1
						.compareToIgnoreCase(program2));
		return programList;
	}

	/**
	 * Parses a Macintosh process line. Process that are not stored in
	 * /Applications and do not end on .app are excluded.
	 * 
	 * @param processLine
	 * @return A process name, NULL if process is excluded
	 */
	private String parseMacProcessLine(String processLine) {

		if (processLine.contains("/Applications/")
				&& processLine.contains(".app")) {

			return processLine.substring(processLine.indexOf('/'),
					processLine.indexOf(".app") + 4);
		}

		return null;
	}

	/**
	 * Parses a Windows process line.
	 * 
	 * @param processLine
	 * @return A process name with window title
	 */
	private String parseWindowsProcessLine(String processLine) {
		String[] columns = processLine.split(",");
		String program = columns[0].replace("\"", "");
		String pid = columns[1].replace("\"", "");
		String windowTitle = columns[8].replace("\"", "");

		return (program + ": " + windowTitle + " " + PIDSEPARATOR + pid);
	}

	public GUIAutomation[] getGuiAutomations() {
		return guiAutomations;
	}

	public float getMinSimilarity() {
		return minSimilarity;
	}

}
