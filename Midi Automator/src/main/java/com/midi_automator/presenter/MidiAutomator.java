package com.midi_automator.presenter;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.Main;
import com.midi_automator.Resources;
import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.guiautomator.GUIAutomator;
import com.midi_automator.midi.MidiINAutomationReceiver;
import com.midi_automator.midi.MidiINDetector;
import com.midi_automator.midi.MidiINExecuteReceiver;
import com.midi_automator.midi.MidiINLearnReceiver;
import com.midi_automator.midi.MidiINMetronomReceiver;
import com.midi_automator.model.IModel;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.SetList;
import com.midi_automator.model.SetListItem;
import com.midi_automator.model.TooManyEntriesException;
import com.midi_automator.utils.FileUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

@Controller
public class MidiAutomator {

	static Logger log = Logger.getLogger(MidiAutomator.class.getName());

	@Autowired
	private Resources resources;

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	private IModel model;

	@Autowired
	private MainFrame mainFrame;

	private int currentItem = -1;
	private List<String> infoMessages;
	private String loadedMidautoFilePath;

	// midi
	private boolean midiLearn;
	private boolean doNotExecuteMidiMessage;
	private JComponent learningComponent;
	private Map<String, MidiDevice> midiDevices = new HashMap<String, MidiDevice>();
	private Map<String, Set<Receiver>> midiFunctionReceiverMapping = new HashMap<String, Set<Receiver>>();

	// gui automation
	private GUIAutomation[] guiAutomations;
	private List<GUIAutomator> guiAutomators;

	// configurations
	public static final String SET_LIST_FILE_EXTENSION = ".mido";
	public static final String[] MIDI_AUTOMATOR_FILE_EXTENSIONS = { "midauto",
			"MIDAUTO" };
	public static final String MIDI_AUTOMATOR_FILE_TYPE = "Midi Automator (midauto)";
	public static final String SWITCH_DIRECTION_PREV = "previous";
	public static final String SWITCH_DIRECTION_NEXT = "next";
	public static final int OPEN_FILE_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int OPEN_FILE_MIDI_CHANNEL = 1;
	public static final int OPEN_FILE_MIDI_CONTROL_NO = 102;
	public static final int SWITCH_NOTIFIER_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int SWITCH_NOTIFIER_MIDI_CHANNEL = 1;
	public static final int SWITCH_NOTIFIER_MIDI_CONTROL_NO = 103;
	public static final int SWITCH_NOTIFIER_MIDI_VALUE = 127;
	public static final int ITEM_SEND_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int ITEM_SEND_MIDI_CHANNEL = 16;
	public static final int ITEM_SEND_MIDI_VALUE = 127;
	public static final long WAIT_BEFORE_OPENING = 100;
	public static final long WAIT_BEFORE_SLAVE_SEND = 2000;
	public static final String OPEN_FILE_MIDI_SIGNATURE = "channel 1: CONTROL CHANGE 102";
	public static final String SWITCH_NOTIFIER_MIDI_SIGNATURE = "channel 1: CONTROL CHANGE 103";
	public static final String METRONOM_FIRST_CLICK_MIDI_SIGNATURE = "channel 16: NOTE ON A4";
	public static final String METRONOM_CLICK_MIDI_SIGNATURE = "channel 16: NOTE ON E4";

	public MidiAutomator() {

		infoMessages = new ArrayList<String>();
		midiLearn = false;
		doNotExecuteMidiMessage = false;
		guiAutomators = new ArrayList<GUIAutomator>();
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
		reloadSetList();
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
		loadGUIAutomationsProperties();

		// MIDI IN Automation Triggers
		for (int i = 0; i < guiAutomations.length; i++) {
			loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
					+ "_" + i);
		}

		mainFrame.reload();
	}

	/**
	 * Loads the model file.
	 */
	private void reloadSetList() {
		try {
			model.load();
			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_IO));
			removeInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_MIDO_FILE_TOO_BIG));

			List<SetListItem> items = model.getSetList().getItems();
			List<String> entryNames = new ArrayList<String>();
			List<String> midiListeningSignatures = new ArrayList<String>();
			List<String> midiSedningSignatures = new ArrayList<String>();

			for (SetListItem item : items) {
				entryNames.add(item.getName());
				midiListeningSignatures.add(item.getMidiListeningSignature());
				midiSedningSignatures.add(item.getMidiSendingSignature());
			}

			mainFrame.setFileEntries(entryNames);
			mainFrame.setMidiListeningSignatures(midiListeningSignatures);
			mainFrame.setMidiSendingSignatures(midiSedningSignatures);

		} catch (FileNotFoundException e1) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		} catch (IOException e1) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		} catch (TooManyEntriesException e) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_MIDO_FILE_TOO_BIG));
		}

		mainFrame.setSelectedIndex(mainFrame.getLastSelectedIndex());
		mainFrame.reload();
	}

	/**
	 * Saves the model to a file.
	 */
	private void saveSetList() {

		removeInfoMessage(Messages.builtMessages
				.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		removeInfoMessage(Messages.builtMessages
				.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		try {
			model.save();
		} catch (FileNotFoundException e) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
		} catch (IOException e) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_MIDO_FILE_IO));
		}
	}

	/**
	 * Loads all GUI automations from the properties.
	 */
	private void loadGUIAutomationsProperties() {

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
				loadMidiDeviceByFunctionKey(
						MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
								+ "_" + index,
						trigger.replace(GUIAutomation.CLICKTRIGGER_MIDI, ""));
			}
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

		// remove all existing GUI automators
		for (int i = 0; i < guiAutomators.size(); i++) {
			guiAutomators.get(i).terminate();
		}
		guiAutomators.clear();

		// generate GUI automators
		for (int i = 0; i < guiAutomations.length; i++) {
			GUIAutomator guiAutomator = new GUIAutomator();
			guiAutomator.setName("GUIAutomator " + i);
			guiAutomator.setGUIAutomation(guiAutomations[i]);
			guiAutomators.add(guiAutomator);
			guiAutomator.start();
		}
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
	 * Loads a MIDI device
	 * 
	 * @param deviceName
	 *            The name of the midi device
	 * @param functionKey
	 *            The key name of the midi function
	 * @param receivers
	 *            A set of midi receivers for the device
	 * @param direction
	 *            The direction "IN"/"OUT" of the midi device
	 * @param loadingErrorMessage
	 *            An error String for loading failures
	 */
	private void loadMidiDevice(String deviceName, String functionKey,
			Set<Receiver> receivers, String direction,
			String loadingErrorMessage) {

		// get old MidiDevice
		MidiDevice oldMidiDevice = midiDevices.get(functionKey);
		String oldDeviceName = null;

		if (oldMidiDevice != null) {
			oldDeviceName = midiDevices.get(functionKey).getDeviceInfo()
					.getName();
		}

		if (!deviceName.equals(oldDeviceName)) {

			// unregister old function
			midiDevices.put(functionKey, null);

			// load new device
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
				try {
					MidiDevice device = MidiUtils.getMidiDevice(deviceName,
							direction);

					if (!device.isOpen()) {
						device.open();
						log.info("Opened MIDI " + direction + " device "
								+ device.getDeviceInfo().getName());
					}

					if (receivers != null) {
						for (Receiver receiver : receivers) {
							connectMidiDeviceWithReceiver(device, receiver,
									direction);
							midiFunctionReceiverMapping.put(functionKey,
									receivers);
						}
					}

					// register new device for function
					midiDevices.put(functionKey, device);

					removeInfoMessage(loadingErrorMessage);

				} catch (MidiUnavailableException e) {
					setInfoMessage(loadingErrorMessage);
				}
			} else {
				midiDevices.remove(functionKey);
			}

			// unload old device
			unloadMidiDevice(oldDeviceName, functionKey, direction);
		}
	}

	/**
	 * Unloads a midi device
	 * 
	 * @param deviceName
	 *            The name of the device
	 * @param functionKey
	 *            The name of the function key
	 * @param midiDirection
	 *            The direction of the midi port "IN"/"OUT"
	 */
	private void unloadMidiDevice(String deviceName, String functionKey,
			String midiDirection) {

		if (deviceName != null
				&& !deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
			try {
				MidiDevice device = MidiUtils.getMidiDevice(deviceName,
						midiDirection);

				removeReceiversFromDevice(device,
						midiFunctionReceiverMapping.get(functionKey));

				if (!midiDevices.containsValue(device)) {
					device.close();

					log.info("Closed MIDI "
							+ MidiUtils.getDirectionOfMidiDevice(device)
							+ " device: " + device.getDeviceInfo().getName());
				}

			} catch (MidiUnavailableException e) {
				log.error("Unloading MIDI device failed.", e);
			}
		}
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
		loadMidiDeviceByFunctionKey(propertyKey, propertyValue);
	}

	/**
	 * Loads a midi device property by opening and connecting the configured
	 * midi devices.
	 * 
	 * @param functionKey
	 *            The function key for the midi device
	 * @param midiDeviceName
	 *            The midi device name
	 */
	public void loadMidiDeviceByFunctionKey(String functionKey,
			String midiDeviceName) {

		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, midiDeviceName);

		Messages.builtMessages.put(functionKey + "_UNVAILABLE",
				errMidiDeviceNotAvailable);

		if (midiDeviceName != null && !midiDeviceName.equals("")) {

			// MIDI IN Remote
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(new MidiINLearnReceiver(this));
				receivers.add(new MidiINExecuteReceiver(this));

				loadMidiDevice(midiDeviceName, functionKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI IN Automation Trigger
			if (functionKey
					.contains(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(new MidiINLearnReceiver(this));
				MidiINAutomationReceiver automationReceiver = new MidiINAutomationReceiver(
						this);
				automationReceiver.setName(functionKey);
				receivers.add(automationReceiver);

				loadMidiDevice(midiDeviceName, functionKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI IN Metronom
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(new MidiINMetronomReceiver(this));

				loadMidiDevice(midiDeviceName, functionKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Remote
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)) {

				loadMidiDevice(midiDeviceName, functionKey, null, "OUT",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Switch Notifier
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE)) {

				loadMidiDevice(midiDeviceName, functionKey, null, "OUT",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Switch Items
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE)) {

				loadMidiDevice(midiDeviceName, functionKey, null, "OUT",
						errMidiDeviceNotAvailable);
			}

		}

	}

	/**
	 * Connects a device with a receiver and opens the device
	 * 
	 * @param device
	 *            The midi device
	 * @param receiver
	 *            The receiver
	 * @param direction
	 *            The direction of the midi device
	 * @throws MidiUnavailableException
	 *             If the midi device is not available
	 */
	private void connectMidiDeviceWithReceiver(MidiDevice device,
			Receiver receiver, String direction)
			throws MidiUnavailableException {

		if (device != null) {

			MidiUtils.setReceiverToDevice(device, receiver);

			log.info("Connected " + receiver.getClass().getSimpleName()
					+ " with " + device.getDeviceInfo().getName());

			// connect MIDI IN detector
			if (direction.equals("IN")) {
				Receiver midiINDetector = new MidiINDetector(this);

				if (!MidiUtils.isReceiverUsedByDevice(device, midiINDetector)) {

					MidiUtils.setReceiverToDevice(device, midiINDetector);

					log.info("Connected "
							+ midiINDetector.getClass().getSimpleName()
							+ " with " + device.getDeviceInfo().getName());
				}
			}

		} else {
			throw new MidiUnavailableException();
		}
	}

	/**
	 * Removes the receivers from the device
	 * 
	 * @param device
	 *            The midi device
	 * @param registeredReceivers
	 *            The midi receivers
	 * @throws MidiUnavailableException
	 *             If the midi device is not available
	 * 
	 */
	private void removeReceiversFromDevice(MidiDevice device,
			Set<Receiver> registeredReceivers) throws MidiUnavailableException {

		if (device != null) {

			if (registeredReceivers != null) {
				for (Receiver receiver : registeredReceivers) {
					MidiUtils.removeReceiverFromDevice(device, receiver
							.getClass().getName());

					log.info("Removed " + receiver.getClass().getSimpleName()
							+ " from " + device.getDeviceInfo().getName());
				}
			}

			// remove MIDI IN detector
			List<Transmitter> transmitters = device.getTransmitters();

			if (transmitters.size() <= 1) {

				Receiver detectorReceiver = null;
				if (transmitters.size() > 0) {
					detectorReceiver = transmitters.get(0).getReceiver();
				}

				if (detectorReceiver != null) {
					MidiUtils.removeReceiverFromDevice(device, detectorReceiver
							.getClass().getName());

					log.info("Removed "
							+ detectorReceiver.getClass().getSimpleName()
							+ " from " + device.getDeviceInfo().getName());
				}
			}
		}
	}

	/**
	 * Returns if the application is in midi learn mode
	 * 
	 * @return <TRUE> application is in midi learn mode, <FALSE> application is
	 *         not in midi learn mode
	 */
	public boolean isInMidiLearnMode() {
		return midiLearn;
	}

	/**
	 * Sets the application to midi learn mode
	 * 
	 * @param midiLearn
	 *            <TRUE> application is in midi learn mode, <FALSE> application
	 *            is not in midi learn mode
	 * @param learningComponent
	 *            The component for which shall be learned, may be <NULL> if
	 *            midi learn is <FALSE>
	 */
	public void setMidiLearnMode(boolean midiLearn, JComponent learningComponent) {
		this.midiLearn = midiLearn;
		this.learningComponent = learningComponent;

		if (midiLearn) {
			setDoNotExecuteMidiMessage(true);
			mainFrame.setInfoText(Messages.MSG_MIDI_LEARN_MODE);
			mainFrame.midiLearnOn(learningComponent);
		} else {
			mainFrame.midiLearnOff();
		}
	}

	/**
	 * Sets the midi signature. The component will be implicitly taken from the
	 * learningComponent field
	 * 
	 * @param signature
	 *            The midi signature
	 */
	public void setMidiSignature(String signature) {
		setMidiSignature(signature, learningComponent);
	}

	/**
	 * Sets the midi signature for a given Component
	 * 
	 * @param midiSignature
	 *            The midi signature
	 * @param component
	 *            The component to set the midi signature for
	 */
	public void setMidiSignature(String midiSignature, Component component) {

		// learning for file list
		if (component instanceof JList) {
			JList<?> list = (JList<?>) component;

			// check for unique signature
			if (isMidiSignatureAlreadyStored(midiSignature)) {
				return;
			}

			model.getSetList().getItems().get(list.getSelectedIndex())
					.setMidiListeningSignature(midiSignature);
			saveSetList();
		}

		// learning for switch buttons
		if (component instanceof JButton) {
			JButton button = (JButton) component;

			// check for unique signature
			if (isMidiSignatureAlreadyStored(midiSignature)) {
				return;
			}

			if (button.getName().equals(MainFrame.NAME_PREV_BUTTON)) {
				properties.setProperty(
						MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE,
						midiSignature);
			}

			if (button.getName().equals(MainFrame.NAME_NEXT_BUTTON)) {
				properties.setProperty(
						MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE,
						midiSignature);
			}

			storePropertiesFile();
			reloadProperties();
		}

		// learning for automation list
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {
			mainFrame.setMidiSignature(midiSignature, component);
		}
	}

	/**
	 * Unsets the midi signature for a given Component
	 * 
	 * @param component
	 *            The component to unset the midi signature for
	 */
	public void unsetMidiSignature(Component component) {
		setMidiSignature(null, component);
		mainFrame.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
				mainFrame.getMidiComponentName(component)));
	}

	/**
	 * Runs the function for the midi message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void executeMidiMessage(MidiMessage message) {

		// do not execute while midi learning
		if (!isInMidiLearnMode()) {
			String signature = MidiUtils.messageToString(message);

			// open file from list
			int index = -1;
			index = model.getSetList().getMidiListeningSignatures()
					.indexOf(signature);
			if (index > -1) {
				log.debug("Open file with index: " + index);
				openFileByIndex(index, true);
				return;
			}

			// switch file
			String prevSignature = getMidiListeningSignature(SWITCH_DIRECTION_PREV);
			String nextSignature = getMidiListeningSignature(SWITCH_DIRECTION_NEXT);

			if (prevSignature != null) {
				if (prevSignature.equals(signature)) {
					openPreviousFile();
				}
			}

			if (nextSignature != null) {
				if (nextSignature.equals(signature)) {
					openNextFile();
				}
			}

			// open file from master
			if (message instanceof ShortMessage) {
				ShortMessage shortMessage = (ShortMessage) message;

				if (shortMessage.getCommand() == OPEN_FILE_MIDI_COMMAND
						&& (shortMessage.getChannel() + 1) == OPEN_FILE_MIDI_CHANNEL
						&& shortMessage.getData1() == OPEN_FILE_MIDI_CONTROL_NO) {

					index = shortMessage.getData2();
					openFileByIndex(index, false);
				}
			}
		}
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
	 * Opens the previous file in the list
	 */
	public void openPreviousFile() {

		currentItem--;

		// cycle list
		if (currentItem < 0) {
			currentItem = (model.getSetList().getItems().size() - 1);
		}
		openFileByIndex(currentItem, true);
	}

	/**
	 * Opens the next file in the list
	 */
	public void openNextFile() {

		currentItem++;

		// cylce list
		if (currentItem >= model.getSetList().getItems().size()) {
			currentItem = 0;
		}
		openFileByIndex(currentItem, true);
	}

	/**
	 * Opens a file from the file list
	 * 
	 * @param index
	 *            The index of the file to open from the list
	 * @param send
	 *            <TRUE> opened index will be sent to slaves, <FALSE> index will
	 *            not be sent
	 */
	public void openFileByIndex(int index, boolean send) {

		try {

			if (!model.getSetList().getItems().isEmpty()) {
				SetListItem item = model.getSetList().getItems().get(index);

				removeInfoMessage(Messages.builtMessages
						.get(Messages.KEY_INFO_ENTRY_OPENED));
				String infoEntryOpened = String.format(
						Messages.MSG_OPENING_ENTRY, item.getName());
				Messages.builtMessages.put(Messages.KEY_INFO_ENTRY_OPENED,
						infoEntryOpened);

				mainFrame.setSelectedIndex(index);
				currentItem = index;

				// Send MIDI change notifier
				sendItemChangeNotifier(midiDevices
						.get(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE));

				// activate per change triggered automations
				for (GUIAutomator guiAutomator : guiAutomators) {
					guiAutomator.activateOncePerChangeAutomations();
				}

				// Send MIDI remote open command
				if (send) {
					new SendItemChangeToSlavesThread(index).start();
				}

				// wait a little before opening file...
				try {
					Thread.sleep(WAIT_BEFORE_OPENING);
				} catch (InterruptedException e) {
					log.error("Delay before opening a file failed.", e);
				}

				// Send MIDI item signature
				sendItemSignature(
						midiDevices
								.get(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE),
						item.getMidiSendingSignature());

				String filePath = item.getFilePath();

				String path = resources.generateRelativeLoadingPath(filePath);

				removeInfoMessage(Messages.builtMessages
						.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
				removeInfoMessage(Messages.builtMessages
						.get(Messages.KEY_ERROR_ITEM_FILE_IO));

				String errFileNotFound = String.format(
						Messages.MSG_FILE_LIST_NOT_FOUND, filePath);
				String errFileNotReadable = String.format(
						Messages.MSG_FILE_LIST_NOT_READABLE, filePath);

				Messages.builtMessages
						.put(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND,
								errFileNotFound);
				Messages.builtMessages.put(Messages.KEY_ERROR_ITEM_FILE_IO,
						errFileNotReadable);

				if (!path.equals("")) {
					log.info("Opening file: " + path);
					FileUtils.openFileFromPath(path);
					setInfoMessage(infoEntryOpened);
				}
			}
		} catch (IllegalArgumentException ex) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
			log.error(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND), ex);
		} catch (IOException ex) {
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_ITEM_FILE_IO));
			log.error(
					Messages.builtMessages.get(Messages.KEY_ERROR_ITEM_FILE_IO),
					ex);
		}
	}

	/**
	 * Sends a midi message with the current index.
	 * 
	 * @param index
	 *            The index
	 */
	private void sendItemChangeToSlaves(int index) {

		String deviceName = getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, deviceName);

		if (deviceName != null) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {

				try {
					ShortMessage message = new ShortMessage();
					message.setMessage(OPEN_FILE_MIDI_COMMAND,
							OPEN_FILE_MIDI_CHANNEL - 1,
							OPEN_FILE_MIDI_CONTROL_NO, index);

					long timeStamp = midiDevices.get(
							MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)
							.getMicrosecondPosition();
					midiDevices
							.get(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)
							.getReceiver().send(message, timeStamp);
					showMidiOUTSignal();

					log.debug("Send MIDI message: "
							+ MidiUtils.messageToString(message));

					removeInfoMessage(errMidiDeviceNotAvailable);

				} catch (MidiUnavailableException e) {

					log.error(errMidiDeviceNotAvailable, e);

					setInfoMessage(errMidiDeviceNotAvailable);
					Messages.builtMessages.put(
							Messages.KEY_MIDI_OUT_REMOTE_DEVICE_UNAVAILABLE,
							errMidiDeviceNotAvailable);

				} catch (InvalidMidiDataException e) {
					log.error("Sending invalid MIDI message to slaves", e);
				}
			}
		}
	}

	/**
	 * Sends a midi message as notifier that the item has changed.
	 * 
	 * @param device
	 *            The midi notification device
	 */
	public void sendItemChangeNotifier(MidiDevice device) {

		if (device != null) {

			String errMidiDeviceNotAvailable = String.format(
					Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, device
							.getDeviceInfo().getName());
			try {
				log.info("Send item change notifier...");
				ShortMessage message = new ShortMessage();
				message.setMessage(SWITCH_NOTIFIER_MIDI_COMMAND,
						SWITCH_NOTIFIER_MIDI_CHANNEL - 1,
						SWITCH_NOTIFIER_MIDI_CONTROL_NO,
						SWITCH_NOTIFIER_MIDI_VALUE);

				sendMidiMessage(device, message);
				removeInfoMessage(errMidiDeviceNotAvailable);

			} catch (MidiUnavailableException e) {

				setInfoMessage(errMidiDeviceNotAvailable);
				Messages.builtMessages
						.put(Messages.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_UNAVAILABLE,
								errMidiDeviceNotAvailable);
				log.error(errMidiDeviceNotAvailable, e);

			} catch (InvalidMidiDataException e) {
				log.error("Send invalid MIDI messsage as change notifier", e);
			}
		}
	}

	/**
	 * Sends the midi signature a specific item from the list.
	 * 
	 * @param itemNo
	 *            The number of the item in the list
	 */
	public void sendItemSignature(int itemNo) {
		SetListItem item = model.getSetList().getItems().get(itemNo);
		sendItemSignature(
				midiDevices
						.get(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE),
				item.getMidiSendingSignature());
	}

	/**
	 * Sends the midi message of the item.
	 * 
	 * @param device
	 *            The midi item device
	 * @param signature
	 *            The item's midi signature
	 */
	private void sendItemSignature(MidiDevice device, String signature) {

		if (device != null) {

			String errMidiDeviceNotAvailable = String.format(
					Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, device
							.getDeviceInfo().getName());
			try {
				MidiMessage message = MidiUtils.signatureToMessage(signature);

				log.info("Sending MIDI signature of item: " + signature);
				sendMidiMessage(device, message);
				removeInfoMessage(errMidiDeviceNotAvailable);

			} catch (MidiUnavailableException e) {

				setInfoMessage(errMidiDeviceNotAvailable);
				Messages.builtMessages.put(
						Messages.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE_UNAVAILABLE,
						errMidiDeviceNotAvailable);
				log.error(errMidiDeviceNotAvailable, e);

			} catch (InvalidMidiDataException e) {
				log.error("Send invalid MIDI message for item", e);
			}
		}
	}

	/**
	 * Sends a midi signature to a given device
	 * 
	 * @param device
	 *            The midi device
	 * @param midiSignature
	 *            The midi signature
	 */
	private void sendMidiMessage(MidiDevice device, MidiMessage message)
			throws MidiUnavailableException {

		long timeStamp = device.getMicrosecondPosition();
		device.open();
		device.getReceiver().send(message, timeStamp);
		showMidiOUTSignal();

		log.debug("Send MIDI message: " + MidiUtils.messageToString(message));
	}

	/**
	 * Gets the midi listening signature of the switch directions
	 * 
	 * @param switchDirection
	 *            SWITCH_DIRECTION_PREV previous direction,
	 *            SWITCH_DIRECTION_NEXT next direction
	 * @return The midi signature
	 */
	public String getMidiListeningSignature(String switchDirection) {

		if (switchDirection.equals(SWITCH_DIRECTION_PREV)) {
			return properties
					.getProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);
		}

		if (switchDirection.equals(SWITCH_DIRECTION_NEXT)) {
			return properties
					.getProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE);
		}

		return null;
	}

	/**
	 * Gets the midi listening signature for the file list index
	 * 
	 * @param index
	 *            The index in the file list
	 * @return The midi signature
	 */
	public String getMidiListeningSignature(int index) {
		try {
			SetList setList = model.getSetList();
			List<SetListItem> list = setList.getItems();
			return list.get(index).getMidiListeningSignature();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the midi signature a file list index is sending
	 * 
	 * @param index
	 *            The index in the file list
	 * @return The midi signature
	 */
	public String getMidiSendingSignature(int index) {
		try {
			SetList setList = model.getSetList();
			List<SetListItem> list = setList.getItems();
			return list.get(index).getMidiSendingSignature();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the entry name of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the name of the file entry
	 */
	public String getEntryNameByIndex(int index) {
		try {
			return model.getSetList().getItems().get(index).getName();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Gets the entry file path of the file list by index
	 * 
	 * @param index
	 *            The index
	 * @return the file path of the entry
	 */
	public String getEntryFilePathByIndex(int index) {
		try {
			return model.getSetList().getItems().get(index).getFilePath();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Sets the name of a midi out device
	 * 
	 * @param deviceName
	 *            The name of the midi device
	 * @param deviceKey
	 *            The key to store the mdidi device
	 */
	public void setMidiDeviceName(String deviceName, String deviceKey) {

		properties.setProperty(deviceKey, deviceName);
		storePropertiesFile();
	}

	/**
	 * Gets the name of a midi device by a properties key
	 * 
	 * @param key
	 *            The properties key
	 * @return The name of the midid device
	 */
	public String getMidiDeviceName(String key) {
		loadPropertiesFile();
		return (String) properties.get(key);
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

		removeInfoMessage(Messages.builtMessages
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
						&& !key.contains(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES)) {
					found = true;
				}
			}
		}

		if (model.getSetList().getMidiListeningSignatures().contains(signature)) {
			found = true;
		}

		if (signature.contains(OPEN_FILE_MIDI_SIGNATURE)) {
			found = true;
		}

		if (found == true) {
			String errDuplicateMidiSignature = String.format(
					Messages.MSG_DUPLICATE_MIDI_SIGNATURE, signature);
			Messages.builtMessages.put(
					Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE,
					errDuplicateMidiSignature);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE));
		}

		return found;
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
		mainFrame.setInfoText(messagesToString(infoMessages));
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
	 * Indicates a midi IN signal
	 */
	public void showMidiINSignal() {
		mainFrame.flashMidiINDetect();
	}

	/**
	 * Indicates a midi OUT signal
	 */
	public void showMidiOUTSignal() {
		mainFrame.flashMidiOUTDetect();
	}

	/**
	 * Returns the resources handler
	 * 
	 * @return The resources handler
	 */
	public Resources getResources() {
		return resources;
	}

	/**
	 * Returns the main program frame
	 * 
	 * @return The main frame
	 */
	public MainFrame getProgramFrame() {
		return mainFrame;
	}

	/**
	 * Pushes the item at the given index one step up
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveUpItem(int index) {

		model.getSetList().exchangeIndexes(index, index - 1);
		saveSetList();
		reloadSetList();
	}

	/**
	 * Pushes the item at the given index one step down
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveDownItem(int index) {

		model.getSetList().exchangeIndexes(index, index + 1);
		saveSetList();
		reloadSetList();
	}

	/**
	 * Deletes the item at the given index from the model
	 * 
	 * @param index
	 *            index of the item
	 */
	public void deleteItem(int index) {

		model.getSetList().getItems().remove(index);
		saveSetList();
		reloadSetList();
	}

	/**
	 * Adds a new item
	 * 
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 */
	public void addItem(String entryName, String filePath,
			String midiSendingSignature) {
		setItem(null, entryName, filePath, "", midiSendingSignature);
	}

	/**
	 * Sets the attributes of an existing item
	 * 
	 * @param index
	 *            the index of the entry
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param midiListeningSignature
	 *            the midi signature the item is listening to
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 * 
	 */
	public void setItem(Integer index, String entryName, String filePath,
			String midiListeningSignature, String midiSendingSignature) {

		insertItem(index, entryName, filePath, midiListeningSignature,
				midiSendingSignature, true);
	}

	/**
	 * Inserts an item
	 * 
	 * @param index
	 *            the index of the entry
	 * @param entryName
	 *            the name of the entry
	 * @param filePath
	 *            the path to the file
	 * @param midiListeningSignature
	 *            the midi signature the item is listening to
	 * @param midiSendingSignature
	 *            the midi signature the item will send on opening
	 * @param overwrite
	 *            <TRUE> the item will be overwritten, <FALSE> the item will be
	 *            inserted
	 * 
	 */
	public void insertItem(Integer index, String entryName, String filePath,
			String midiListeningSignature, String midiSendingSignature,
			boolean overwrite) {

		removeInfoMessage(Messages.builtMessages
				.get(Messages.KEY_ERROR_TOO_MUCH_ENTRIES));

		if (index == null && model.getSetList().getItems().size() >= 128) {
			String error = String.format(Messages.MSG_FILE_LIST_IS_FULL,
					entryName);
			Messages.builtMessages.put(Messages.KEY_ERROR_TOO_MUCH_ENTRIES,
					error);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_TOO_MUCH_ENTRIES));
			return;
		}

		if (entryName != null && !entryName.equals("")) {
			SetListItem item = new SetListItem(entryName, filePath,
					midiListeningSignature, midiSendingSignature);

			if (overwrite) {
				model.getSetList().setItem(item, index);
				log.debug("Set item on index: " + index + ", entry: "
						+ entryName + ", file: " + filePath + ", listening: \""
						+ midiListeningSignature + "\", sending: \""
						+ midiSendingSignature + "\"");
			} else {
				model.getSetList().addItem(item, index);
				log.debug("Add item on index: " + index + ", entry: "
						+ entryName + ", file: " + filePath + ", listening: \""
						+ midiListeningSignature + "\", sending: \""
						+ midiSendingSignature + "\"");
			}

			saveSetList();
		}

		reloadSetList();
	}

	/**
	 * Gets a unique midi signature that is not used for any item yet.
	 * 
	 * @return The midi signature, <NULL> if there is no unique signature left
	 */
	public String getUniqueSendingMidiSignature() {

		List<String> signatures = model.getSetList().getMidiSendingSignatures();

		ShortMessage message = new ShortMessage();

		int controlNo = 1;
		boolean found = true;
		String signature = null;
		while (found && controlNo <= 127) {
			try {
				message.setMessage(ShortMessage.CONTROL_CHANGE,
						ITEM_SEND_MIDI_CHANNEL - 1, controlNo,
						ITEM_SEND_MIDI_VALUE);
			} catch (InvalidMidiDataException e) {
				log.error("Created invalid MIDI message for item", e);
			}
			signature = MidiUtils.messageToString(message);

			if (!signatures.contains(signature)) {
				found = false;
			}
			controlNo++;
		}

		return signature;
	}

	/**
	 * Executes the midi metronom's click
	 * 
	 * @beat the current clicked beat
	 */
	public void metronomClick(int beat) {
		if (beat == 1) {
			log.debug("Metronom first click");
			mainFrame.flashFileList(Color.RED);
		} else {
			log.debug("Metronom other click");
			mainFrame.flashFileList(Color.GREEN);
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
		storePropertiesFile();
	}

	/**
	 * Gets the configured GUI automations.
	 * 
	 * @return The GUI automations as an array
	 */
	public GUIAutomation[] getGUIAutomations() {
		return guiAutomations;
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
	private void storePropertiesFile() {

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

	/**
	 * Gets the state if midi messages shall be executed.
	 * 
	 * @return <TRUE> if messages shall not be executed, <FALSE> if they shall
	 *         be executed
	 */
	public boolean isDoNotExecuteMidiMessage() {
		return doNotExecuteMidiMessage;
	}

	/**
	 * Sets the state if midi messages shall be executed.
	 * 
	 * @param doNotExecuteMidiMessage
	 *            <TRUE> if messages shall not be executed, <FALSE> if they
	 *            shall be executed
	 */
	public void setDoNotExecuteMidiMessage(boolean doNotExecuteMidiMessage) {
		this.doNotExecuteMidiMessage = doNotExecuteMidiMessage;
	}

	/**
	 * Saves the set list and the properties to a zip file.
	 * 
	 * @param filePath
	 *            The path to store the zip file
	 */
	public void exportMidautoFile(String filePath) {
		File[] files = new File[] { new File(model.getPersistenceFileName()),
				new File(properties.getPropertiesFilePath()) };
		try {
			FileUtils.zipFiles(files, filePath);
			loadedMidautoFilePath = filePath;
		} catch (ZipException e) {
			log.error("Zipping file " + filePath + " failed.", e);
		} catch (IOException e) {
			log.error("Writing to file " + filePath + " failed.", e);
		}
	}

	/**
	 * Loads the set list and the properties from a zip file.
	 * 
	 * @param file
	 *            The zip file
	 */
	public void importMidautoFile(File file) {

		loadedMidautoFilePath = file.getAbsolutePath();
		String unzipPath = resources.getPropertiesPath();

		if (unzipPath.equals("")) {
			unzipPath = ".";
		}

		try {
			FileUtils.unzipFile(new ZipFile(file), unzipPath);
			reloadProperties();
			reloadSetList();
		} catch (ZipException e) {
			log.error("Unzipping file " + file.getAbsolutePath() + " failed.",
					e);
		} catch (IOException e) {
			log.error("Unzipping file " + file.getAbsolutePath() + " failed.",
					e);
		}
	}

	/**
	 * Sends the changed item to remote slaves with a delay
	 * 
	 * @author aguelle
	 * 
	 */
	class SendItemChangeToSlavesThread extends Thread {

		private int index;

		public SendItemChangeToSlavesThread(int index) {
			this.index = index;
		}

		@Override
		public void run() {
			// wait a little before sending remote message...
			try {
				Thread.sleep(WAIT_BEFORE_SLAVE_SEND);
			} catch (InterruptedException e) {
				log.error("Delay for sending remote message to slaves failed",
						e);
			}

			log.info("Sending open index " + index
					+ " MIDI message to slaves...");

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					sendItemChangeToSlaves(index);
				}
			});
		}
	}

	public String getLoadedMidautoFilePath() {
		return loadedMidautoFilePath;
	}

	public String getLoadedMidautoFileName() {
		if (loadedMidautoFilePath != null) {
			String[] splitted = loadedMidautoFilePath.split(File.separator);
			return splitted[splitted.length - 1];
		}
		return null;
	}
}
