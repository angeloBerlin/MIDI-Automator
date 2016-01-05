package com.midi_automator.presenter.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.guiautomator.GUIAutomator;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Presenter;
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
	private MidiAutomatorProperties properties;

	@Autowired
	private Presenter presenter;

	@Autowired
	private MidiService midiService;

	private GUIAutomation[] guiAutomations;
	private List<GUIAutomator> guiAutomators = new ArrayList<GUIAutomator>();;

	/**
	 * Loads all GUI automations from the properties.
	 */
	public void loadProperties() {

		Set<Entry<Object, Object>> guiAutomationProperties_Images = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES);
		Set<Entry<Object, Object>> guiAutomationProperties_Types = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES);
		Set<Entry<Object, Object>> guiAutomationProperties_Triggers = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS);
		Set<Entry<Object, Object>> guiAutomationProperties_MidiSignatures = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES);
		Set<Entry<Object, Object>> guiAutomationProperties_MinDelays = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS);
		Set<Entry<Object, Object>> guiAutomationProperties_MinSimilarities = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_SIMILARITIES);
		Set<Entry<Object, Object>> guiAutomationProperties_AreMovable = properties
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MOVABLES);

		// initiate array with GUI automations
		guiAutomations = new GUIAutomation[guiAutomationProperties_Images
				.size()];

		for (int i = 0; i < guiAutomations.length; i++) {
			guiAutomations[i] = new GUIAutomation();
		}

		// initiate image paths
		for (Entry<Object, Object> property : guiAutomationProperties_Images) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			guiAutomations[index].setImagePath((String) property.getValue());
		}

		// initiate types
		for (Entry<Object, Object> property : guiAutomationProperties_Types) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			guiAutomations[index].setType((String) property.getValue());
		}

		// initiate triggers
		for (Entry<Object, Object> property : guiAutomationProperties_Triggers) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			String trigger = (String) property.getValue();
			guiAutomations[index].setTrigger(trigger);

			if (trigger.contains(GUIAutomation.CLICKTRIGGER_MIDI)) {
				midiService
						.loadMidiDeviceByFunctionKey(
								MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
										+ "_" + index, trigger.replace(
										GUIAutomation.CLICKTRIGGER_MIDI, ""));
			}
		}

		// MIDI IN Automation Triggers
		for (int i = 0; i < guiAutomations.length; i++) {
			midiService
					.loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
							+ "_" + i);
		}

		// initiate min delays
		for (Entry<Object, Object> property : guiAutomationProperties_MinDelays) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			long minDelay = Long.valueOf((String) property.getValue());
			guiAutomations[index].setMinDelay(minDelay);
		}

		// initiate midi signatures
		for (Entry<Object, Object> property : guiAutomationProperties_MidiSignatures) {

			String key = (String) property.getKey();
			int index = MidiAutomatorProperties.getIndexOfPropertyKey(key);

			String value = (String) property.getValue();

			if (value.equals(MidiAutomatorProperties.VALUE_NULL)) {
				value = null;
			}

			guiAutomations[index].setMidiSignature(value);
		}

		// initiate min similarities
		for (Entry<Object, Object> property : guiAutomationProperties_MinSimilarities) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			float minSimilarity = Float.valueOf((String) property.getValue());
			guiAutomations[index].setMinSimilarity(minSimilarity);
		}

		// initiate is movable
		for (Entry<Object, Object> property : guiAutomationProperties_AreMovable) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			boolean isMovable = Boolean.valueOf((String) property.getValue());
			guiAutomations[index].setMovable(isMovable);
		}

		terminateAllGUIAutomators();

		// generate GUI automators
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < guiAutomations.length; i++) {

					GUIAutomator guiAutomator = new GUIAutomator();
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
	 * Terminates all GUI automators
	 */
	public void terminateAllGUIAutomators() {

		for (int i = 0; i < guiAutomators.size(); i++) {
			GUIAutomator guiAutomator = guiAutomators.get(i);
			guiAutomator.terminate();

			log.debug("Terminate GUI automation: "
					+ guiAutomator.getGuiAutomation());
		}
		guiAutomators.clear();
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
	 */
	public void setGUIAutomations(GUIAutomation[] guiAutomations) {

		removeGUIAutomations();
		this.guiAutomations = guiAutomations;

		for (int i = 0; i < guiAutomations.length; i++) {

			// click image path
			String imagePath = guiAutomations[i].getImagePath();

			if (imagePath == null) {
				imagePath = MidiAutomatorProperties.VALUE_NULL;
			}
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					imagePath);

			// click type
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					guiAutomations[i].getType());

			// click trigger
			String trigger = guiAutomations[i].getTrigger();
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					trigger);

			// min delay
			String minDelay = String.valueOf(guiAutomations[i].getMinDelay());
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					minDelay);

			// midi signature
			String midiSignature = guiAutomations[i].getMidiSignature();

			if (midiSignature == null || midiSignature.equals("")) {
				midiSignature = MidiAutomatorProperties.VALUE_NULL;
			}
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					midiSignature);

			// min similarity
			String minSimilarity = String.valueOf(guiAutomations[i]
					.getMinSimilarity());
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_SIMILARITIES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					minSimilarity);

			// movable
			String isMovable = Boolean.toString(guiAutomations[i].isMovable());
			properties.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MOVABLES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					isMovable);
		}
		presenter.storePropertiesFile();
	}

	/**
	 * Removes all GUI automations.
	 */
	public void removeGUIAutomations() {
		guiAutomations = null;

		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES);
		properties.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES);
		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS);
		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES);
		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS);
		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_SIMILARITIES);
		properties
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MOVABLES);
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
	 * De-/Activates the GUI automation.
	 * 
	 * @param active
	 *            <TRUE> activete GUI automation, <FALSE> deactivate GUI
	 *            automation
	 */
	public void setGUIAutomationsToActive(boolean active) {
		for (GUIAutomator guiAutomator : guiAutomators) {
			guiAutomator.setActive(active);
		}
	}

	public GUIAutomation[] getGuiAutomations() {
		return guiAutomations;
	}

}
