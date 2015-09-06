package com.midi_automator.presenter;

import java.awt.Color;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

import org.apache.log4j.Logger;

import com.midi_automator.Resources;
import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.guiautomator.GUIAutomator;
import com.midi_automator.midi.MidiAutomatorReceiver;
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
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi_automator.view.frames.MainFrame;

public class MidiAutomator {

	static Logger log = Logger.getLogger(MidiAutomator.class.getName());

	private static MidiAutomator instance;

	private final boolean TEST;
	private final String VERSION = "1.1.1";
	private final Resources RESOURCES;
	private final MidiAutomatorProperties PROPERTIES;
	private final String PROPERTIES_FILE_NAME = "midiautomator.properties";
	private final MainFrame PROGRAM_FRAME;

	private IModel model;
	private int currentItem = -1;
	private List<String> infoMessages;

	// midi
	private boolean midiLearn;
	private boolean doNotExecuteMidiMessage;
	private JComponent learningComponent;
	private Map<String, MidiDevice> midiDevices = new HashMap<String, MidiDevice>();
	private Map<String, Set<Receiver>> midiFunctionReceiverMapping = new HashMap<String, Set<Receiver>>();
	private Map<String, MidiAutomatorReceiver> midiReceivers = new HashMap<String, MidiAutomatorReceiver>();
	private final String KEY_MIDI_RECEIVER_MIDI_LEARN = "KEY_MIDI_RECEIVER_MIDI_LEARN";
	private final String KEY_MIDI_RECEIVER_EXECUTE = "KEY_MIDI_RECEIVER_EXECUTE";
	private final String KEY_MIDI_RECEIVER_METRONOM = "KEY_MIDI_RECEIVER_METRONOM";
	private final String KEY_MIDI_RECEIVER_IN_DETECTOR = "KEY_MIDI_RECEIVER_IN_DETECTOR";

	// gui automation
	private GUIAutomation[] guiAutomations;
	private List<GUIAutomator> guiAutomators;

	// configurations
	public static final String FILE_EXTENSION = ".mido";
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

	/**
	 * Constructor
	 * 
	 * @param model
	 *            The model of the application
	 * @param resources
	 *            The resources with working directory and operating system
	 * @param fileName
	 *            The name of the file list
	 * @param test
	 *            <TRUE> Program GUI will be opened on left monitor side with
	 *            top margin, <FALSE> normal mode
	 */
	private MidiAutomator(IModel model, Resources resources, String fileName,
			boolean test) {

		this.model = model;
		RESOURCES = resources;
		this.TEST = test;

		PROPERTIES = new MidiAutomatorProperties(RESOURCES.getPropertiesPath()
				+ PROPERTIES_FILE_NAME);

		infoMessages = new ArrayList<String>();

		model.setPersistenceFileName(fileName);

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

		midiLearn = false;
		doNotExecuteMidiMessage = false;
		midiReceivers.put(KEY_MIDI_RECEIVER_MIDI_LEARN,
				new MidiINLearnReceiver(this));
		midiReceivers.put(KEY_MIDI_RECEIVER_EXECUTE, new MidiINExecuteReceiver(
				this));
		midiReceivers.put(KEY_MIDI_RECEIVER_METRONOM,
				new MidiINMetronomReceiver(this));
		midiReceivers.put(KEY_MIDI_RECEIVER_IN_DETECTOR, new MidiINDetector(
				this));

		PROGRAM_FRAME = new MainFrame(this, VERSION);

		guiAutomators = new ArrayList<GUIAutomator>();

		load();
		reloadProperties();
	}

	/**
	 * @param model
	 *            The model of the application
	 * @param resources
	 *            The resources with working directory and operating system
	 * @param fileName
	 *            The name of the file list
	 * @param test
	 *            <TRUE> Program GUI will be opened on left monitor side with
	 *            top margin, <FALSE> normal mode
	 */
	public static MidiAutomator getInstance(IModel model, Resources resources,
			String fileName, boolean test) {

		if (instance == null) {
			instance = new MidiAutomator(model, resources, fileName, test);
		}
		return instance;
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

		PROGRAM_FRAME.reload();
	}

	/**
	 * Loads the model file.
	 */
	private void load() {
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

			PROGRAM_FRAME.setFileEntries(entryNames);
			PROGRAM_FRAME.setMidiListeningSignatures(midiListeningSignatures);
			PROGRAM_FRAME.setMidiSendingSignatures(midiSedningSignatures);

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

		PROGRAM_FRAME.setSelectedIndex(PROGRAM_FRAME.getLastSelectedIndex());
		PROGRAM_FRAME.reload();
	}

	/**
	 * Saves the model to a file.
	 */
	private void save() {

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

		Set<Entry<Object, Object>> guiAutomationProperties_Images = PROPERTIES
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES);
		Set<Entry<Object, Object>> guiAutomationProperties_Types = PROPERTIES
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES);
		Set<Entry<Object, Object>> guiAutomationProperties_Triggers = PROPERTIES
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS);
		Set<Entry<Object, Object>> guiAutomationProperties_MidiSignatures = PROPERTIES
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES);
		Set<Entry<Object, Object>> guiAutomationProperties_MinDelays = PROPERTIES
				.entrySet(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS);

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
			guiAutomations[index].setTrigger((String) property.getValue());
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

		// initiate min delays
		for (Entry<Object, Object> property : guiAutomationProperties_MinDelays) {
			int index = MidiAutomatorProperties
					.getIndexOfPropertyKey((String) property.getKey());
			long minDelay = Long.valueOf((String) property.getValue());
			guiAutomations[index].setMinDelay(minDelay);
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
			GUIAutomation[] automations = new GUIAutomation[] { guiAutomations[i] };
			guiAutomator.setGUIAutomations(automations);
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

		String propertyValue = (String) PROPERTIES.get(propertyKey);

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
				PROGRAM_FRAME.setButtonTooltip(propertyValue,
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

		String deviceName = PROPERTIES.getProperty(propertyKey);

		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, deviceName);

		Messages.builtMessages.put(propertyKey + "_UNVAILABLE",
				errMidiDeviceNotAvailable);

		if (deviceName != null && !deviceName.equals("")) {

			// MIDI IN Remote
			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(midiReceivers.get(KEY_MIDI_RECEIVER_MIDI_LEARN));
				receivers.add(midiReceivers.get(KEY_MIDI_RECEIVER_EXECUTE));

				loadMidiDevice(deviceName, propertyKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI IN Metronom
			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(midiReceivers.get(KEY_MIDI_RECEIVER_METRONOM));

				loadMidiDevice(deviceName, propertyKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Remote
			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)) {

				loadMidiDevice(deviceName, propertyKey, null, "OUT",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Switch Notifier
			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE)) {

				loadMidiDevice(deviceName, propertyKey, null, "OUT",
						errMidiDeviceNotAvailable);
			}

			// MIDI OUT Switch Items
			if (propertyKey
					.equals(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE)) {

				loadMidiDevice(deviceName, propertyKey, null, "OUT",
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
				Receiver midiINDetector = midiReceivers
						.get(KEY_MIDI_RECEIVER_IN_DETECTOR);

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
					MidiUtils.removeReceiverFromDevice(device, receiver);

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
					MidiUtils
							.removeReceiverFromDevice(device, detectorReceiver);

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
			PROGRAM_FRAME.setInfoText(Messages.MSG_MIDI_LEARN_MODE);
			PROGRAM_FRAME.midiLearnOn(learningComponent);
		} else {
			PROGRAM_FRAME.midiLearnOff();
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

		// check for unique signature
		if (midiSignatureIsAlreadyStored(midiSignature)) {
			return;
		}

		// learning for file list
		if (component instanceof JList) {
			JList<?> list = (JList<?>) component;

			model.getSetList().getItems().get(list.getSelectedIndex())
					.setMidiListeningSignature(midiSignature);
			save();
		}

		// learning for switch buttons
		if (component instanceof JButton) {
			JButton button = (JButton) component;

			if (button.getName().equals(MainFrame.NAME_PREV_BUTTON)) {
				PROPERTIES.setProperty(
						MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE,
						midiSignature);
			}

			if (button.getName().equals(MainFrame.NAME_NEXT_BUTTON)) {
				PROPERTIES.setProperty(
						MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE,
						midiSignature);
			}

			storePropertiesFile();
			reloadProperties();
		}

		// learning for automation list
		if (component.getName().equals(
				GUIAutomationConfigurationPanel.NAME_CONFIGURATION_TABLE)) {
			PROGRAM_FRAME.setMidiSignature(midiSignature, component);
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
		PROGRAM_FRAME.setInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
				PROGRAM_FRAME.getMidiComponentName(component)));
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
			log.debug("Open file with index: " + index);
			if (index > -1) {
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
			// activate midi automations
			for (GUIAutomator guiAutomator : guiAutomators) {
				guiAutomator.activateMidiAutomations(signature);
			}
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

				PROGRAM_FRAME.setSelectedIndex(index);
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

				String path = RESOURCES.generateRelativeLoadingPath(filePath);

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

				if (path.equals("")) {
					setInfoMessage(Messages.builtMessages
							.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
					log.info(Messages.builtMessages
							.get(Messages.KEY_ERROR_ITEM_FILE_NOT_FOUND));
				} else {
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
			return PROPERTIES
					.getProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);
		}

		if (switchDirection.equals(SWITCH_DIRECTION_NEXT)) {
			return PROPERTIES
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

		PROPERTIES.setProperty(deviceKey, deviceName);
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
		return (String) PROPERTIES.get(key);
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
	private boolean midiSignatureIsAlreadyStored(String signature) {

		removeInfoMessage(Messages.builtMessages
				.get(Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE));

		if (signature == null) {
			signature = "";
			return false;
		}

		boolean found = false;

		if (PROPERTIES != null) {
			if (PROPERTIES.containsValue(signature)) {
				found = true;
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
		return TEST;
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
		PROGRAM_FRAME.setInfoText(messagesToString(infoMessages));
	}

	/**
	 * Removes the info message
	 * 
	 * @param message
	 *            The info message
	 */
	public void removeInfoMessage(String message) {
		infoMessages.remove(message);
		PROGRAM_FRAME.setInfoText(messagesToString(infoMessages));
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
		PROGRAM_FRAME.flashMidiINDetect();
	}

	/**
	 * Indicates a midi OUT signal
	 */
	public void showMidiOUTSignal() {
		PROGRAM_FRAME.flashMidiOUTDetect();
	}

	/**
	 * Returns the resources handler
	 * 
	 * @return The resources handler
	 */
	public Resources getResources() {
		return RESOURCES;
	}

	/**
	 * Pushes the item at the given index one step up
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveUpItem(int index) {

		model.getSetList().exchangeIndexes(index, index - 1);
		save();
		load();
	}

	/**
	 * Pushes the item at the given index one step down
	 * 
	 * @param index
	 *            index of the item
	 */
	public void moveDownItem(int index) {

		model.getSetList().exchangeIndexes(index, index + 1);
		save();
		load();
	}

	/**
	 * Deletes the item at the given index from the model
	 * 
	 * @param index
	 *            index of the item
	 */
	public void deleteItem(int index) {

		model.getSetList().getItems().remove(index);
		save();
		load();
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
			model.getSetList().setItem(item, index);
			log.debug("Set item on index: " + index + ", entry: " + entryName
					+ ", file: " + filePath + ", listening: \""
					+ midiListeningSignature + "\", sending: \""
					+ midiSendingSignature + "\"");
			save();
		}

		load();
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
			PROGRAM_FRAME.flashFileList(Color.RED);
		} else {
			PROGRAM_FRAME.flashFileList(Color.GREEN);
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
			PROPERTIES.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					imagePath);

			// click type
			PROPERTIES.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					guiAutomations[i].getType());

			// click trigger
			PROPERTIES.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					guiAutomations[i].getTrigger());

			// midi signature
			String midiSignature = guiAutomations[i].getMidiSignature();

			if (midiSignature == null || midiSignature.equals("")) {
				midiSignature = MidiAutomatorProperties.VALUE_NULL;
			}
			PROPERTIES.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					midiSignature);

			// min delay
			String minDelay = String.valueOf(guiAutomations[i].getMinDelay());
			PROPERTIES.setProperty(
					MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS
							+ MidiAutomatorProperties.INDEX_SEPARATOR + i,
					minDelay);
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

		PROPERTIES
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_IMAGES);
		PROPERTIES.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TYPES);
		PROPERTIES
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_TRIGGERS);
		PROPERTIES
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIDI_SIGNATURES);
		PROPERTIES
				.removeKeys(MidiAutomatorProperties.KEY_GUI_AUTOMATION_MIN_DELAYS);
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

			PROPERTIES.load();

		} catch (FileNotFoundException e) {

			String error = String.format(Messages.MSG_FILE_NOT_FOUND,
					RESOURCES.getPropertiesPath() + PROPERTIES_FILE_NAME);
			Messages.builtMessages.put(
					Messages.KEY_ERROR_PROPERTIES_FILE_NOT_FOUND, error);
			setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_PROPERTIES_FILE_NOT_FOUND));

		} catch (IOException e) {

			String error = String.format(Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					RESOURCES.getPropertiesPath() + PROPERTIES_FILE_NAME);
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

			PROPERTIES.store();

		} catch (IOException e) {

			String error = String.format(Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					RESOURCES.getPropertiesPath() + PROPERTIES_FILE_NAME);
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
			sendItemChangeToSlaves(index);
		}
	}
}
