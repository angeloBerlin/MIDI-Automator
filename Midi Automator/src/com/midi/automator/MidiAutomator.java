package com.midi.automator;

import java.awt.Color;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;

import com.midi.automator.guiautomator.GUIAutomation;
import com.midi.automator.guiautomator.GUIAutomator;
import com.midi.automator.midi.MidiAutomatorReceiver;
import com.midi.automator.midi.MidiINDetector;
import com.midi.automator.midi.MidiINExecuteReceiver;
import com.midi.automator.midi.MidiINLearnReceiver;
import com.midi.automator.midi.MidiINMetronomReceiver;
import com.midi.automator.model.IModel;
import com.midi.automator.model.MidiAutomatorProperties;
import com.midi.automator.model.ModelImpl;
import com.midi.automator.utils.FileUtils;
import com.midi.automator.utils.MidiUtils;
import com.midi.automator.utils.SystemUtils;
import com.midi.automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi.automator.view.frames.MainFrame;

public class MidiAutomator implements IApplication {

	private final boolean DEBUG;
	private final boolean DEV;
	private final String VERSION = "1.0.2";
	private final IModel MODEL;
	private final Resources resources;
	private final MidiAutomatorProperties PROPERTIES;
	private final String PROPERTIES_FILE_NAME = "midiautomator.properties";
	private final MainFrame PROGRAM_FRAME;

	private String fileName;
	private List<String> infoMessages;

	// midi
	private boolean midiLearn;
	private boolean doNotExecuteMidiMessage;
	private JComponent learningComponent;
	private String oldMidiINRemoteDeviceSignature;
	private String oldMidiINMetronomDeviceSignature;
	private String oldMidiOUTRemoteDeviceSignature;
	private String oldMidiOUTSwitchNotifierDeviceSignature;
	private MidiDevice midiINRemoteDevice;
	private MidiDevice midiINMetronomDevice;
	private MidiDevice midiOUTRemoteDevice;
	private MidiDevice midiOUTSwitchNotifierDevice;
	private MidiAutomatorReceiver midiLearnReceiver;
	private MidiAutomatorReceiver midiExecuteReceiver;
	private MidiAutomatorReceiver midiMetronomReceiver;
	private MidiAutomatorReceiver midiINDetectorReceiver;

	// gui automation
	private GUIAutomation[] guiAutomations;
	private List<GUIAutomator> guiAutomators;

	// info messages
	private String errDuplicateMidiSignature;
	private String infoEntryOpened;
	private String errMidoFileNotFound;
	private String errMidoFileCouldNotBeOpened;
	private String errFileCouldNotBeAdded;
	private String errPropertiesFileNotFound;
	private String errPropertiesFileCouldNotBeOpened;
	private String errMidiINRemoteDeviceUnavailable;
	private String errMidiINMetronomDeviceUnavailable;
	private String errMidiOUTRemoteDeviceUnavailable;
	private String errMidiOUTSwitchNotifierDeviceUnavailable;

	/**
	 * Constructor
	 * 
	 * @param wd
	 *            The working directory
	 * @param os
	 *            The operating system
	 * @param fileName
	 *            The name of the file list
	 * @param debug
	 *            <TRUE> debug information will be written to console, <FALSE>
	 *            no debug information to console
	 * @param dev
	 *            <TRUE> Program GUI will be opened on second monitor, <FALSE>
	 *            normal mode
	 */
	public MidiAutomator(String wd, String os, String fileName, boolean debug,
			boolean dev) {

		resources = new Resources(os, wd);
		this.fileName = fileName;
		this.DEBUG = debug;
		this.DEV = dev;

		PROPERTIES = new MidiAutomatorProperties(resources.getPropertiesPath()
				+ PROPERTIES_FILE_NAME);

		infoMessages = new ArrayList<String>();
		MODEL = ModelImpl.getInstance(this);
		MODEL.setPersistenceFileName(this.fileName);

		midiLearn = false;
		doNotExecuteMidiMessage = false;
		midiLearnReceiver = new MidiINLearnReceiver(this);
		midiExecuteReceiver = new MidiINExecuteReceiver(this);
		midiMetronomReceiver = new MidiINMetronomReceiver(this);
		midiINDetectorReceiver = new MidiINDetector(this);

		PROGRAM_FRAME = new MainFrame(this, VERSION);

		guiAutomators = new ArrayList<GUIAutomator>();

		reloadModel();
		reloadProperties();
	}

	@Override
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
	private void reloadModel() {
		PROGRAM_FRAME.setFileEntries(MODEL.getEntryNames());
		PROGRAM_FRAME.setMidiSignatures(MODEL.getMidiSignatures());
		PROGRAM_FRAME.setSelectedIndex(PROGRAM_FRAME.getLastSelectedIndex());
		PROGRAM_FRAME.reload();
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
			GUIAutomator guiAutomator = new GUIAutomator(DEBUG);
			guiAutomator.setName("Thread" + i);
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
	 * Gets the old midi device
	 * 
	 * @param oldDeviceName
	 *            Name of of the old device
	 * @param oldDeviceType
	 *            Direction of the old device <"IN"|"OUT">
	 * @param newDeviceName
	 *            The new device name
	 * @return The old device, <NULL> if there is no old device or device has
	 *         not changed
	 * @throws MidiUnavailableException
	 */
	private MidiDevice getOldMidiDevice(String oldDeviceName,
			String oldDeviceType, String newDeviceName) {
		MidiDevice oldDevice = null;

		if (oldDeviceName != null) {
			if (!oldDeviceName.equals(newDeviceName)) {

				try {
					oldDevice = MidiUtils.getMidiDevice(oldDeviceName,
							oldDeviceType);
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
					return null;
				}
			}
		}

		return oldDevice;
	}

	/**
	 * Loads the MIDI IN Remote device
	 * 
	 * @param deviceName
	 *            The name of the midi device
	 * @param error
	 *            The error String
	 */
	private void loadMidiInRemoteDevice(String deviceName, String error) {

		if (!deviceName.equals(oldMidiINRemoteDeviceSignature)) {
			try {

				midiINRemoteDevice = MidiUtils.getMidiDevice(deviceName, "IN");

				// connect MIDI learner
				connectMidiDeviceWithReceiver(midiINRemoteDevice,
						midiLearnReceiver);

				// connect MIDI executer
				connectMidiDeviceWithReceiver(midiINRemoteDevice,
						midiExecuteReceiver);

				removeInfoMessage(errMidiINRemoteDeviceUnavailable);

			} catch (MidiUnavailableException e) {
				if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
					setInfoMessage(error);
					errMidiINRemoteDeviceUnavailable = error;
				}
			}
		}
	}

	/**
	 * Loads the MIDI OUT Remote device
	 * 
	 * @param deviceName
	 *            The name of the device
	 * @param error
	 *            The error String
	 */
	private void loadMidiOutRemoteDevice(String deviceName, String error) {

		try {
			midiOUTRemoteDevice = MidiUtils.getMidiDevice(deviceName, "OUT");

			if (midiOUTRemoteDevice != null) {
				midiOUTRemoteDevice.open();
			}

			removeInfoMessage(errMidiOUTRemoteDeviceUnavailable);
		} catch (MidiUnavailableException e) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
				setInfoMessage(error);
				errMidiOUTRemoteDeviceUnavailable = error;
			}
		}
	}

	/**
	 * Loads the MIDI IN Metronom device
	 * 
	 * @param deviceName
	 *            The name of the device
	 * @param error
	 *            The error String
	 */
	private void loadMidiInMetronomDevice(String deviceName, String error) {

		try {
			midiINMetronomDevice = MidiUtils.getMidiDevice(deviceName, "IN");

			// connect MIDI metronom
			connectMidiDeviceWithReceiver(midiINMetronomDevice,
					midiMetronomReceiver);

			removeInfoMessage(errMidiINMetronomDeviceUnavailable);
		} catch (MidiUnavailableException e) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
				setInfoMessage(error);
				errMidiINMetronomDeviceUnavailable = error;
			}
		}
	}

	/**
	 * Loads the MIDI OUT Switch Notifier
	 * 
	 * @param deviceName
	 *            The name of the device
	 * @param error
	 *            The error String
	 */
	private void loadMidiOutSwitchNotifier(String deviceName, String error) {

		try {
			midiOUTSwitchNotifierDevice = MidiUtils.getMidiDevice(deviceName,
					"OUT");

			removeInfoMessage(errMidiOUTSwitchNotifierDeviceUnavailable);
		} catch (MidiUnavailableException e) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {
				setInfoMessage(error);
				errMidiOUTSwitchNotifierDeviceUnavailable = error;
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

		String propertyValue = PROPERTIES.getProperty(propertyKey);

		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, propertyValue);

		if (propertyValue != null) {
			if (!propertyValue.equals("")) {

				MidiDevice oldDevice = null;

				// MIDI IN Remote
				if (propertyKey
						.equals(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE)) {
					oldDevice = getOldMidiDevice(
							oldMidiINRemoteDeviceSignature, "IN", propertyValue);

					loadMidiInRemoteDevice(propertyValue,
							errMidiDeviceNotAvailable);
				}

				// MIDI OUT Remote
				if (propertyKey
						.equals(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)) {

					oldDevice = getOldMidiDevice(
							oldMidiOUTRemoteDeviceSignature, "OUT",
							propertyValue);
					loadMidiOutRemoteDevice(propertyValue,
							errMidiDeviceNotAvailable);
				}

				// MIDI IN Metronom
				if (propertyKey
						.equals(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE)) {

					oldDevice = getOldMidiDevice(
							oldMidiINMetronomDeviceSignature, "IN",
							propertyValue);
					loadMidiInMetronomDevice(propertyValue,
							errMidiDeviceNotAvailable);
				}

				// MIDI OUT Switch Notifier
				if (propertyKey
						.equals(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE)) {

					oldDevice = getOldMidiDevice(
							oldMidiOUTSwitchNotifierDeviceSignature, "OUT",
							propertyValue);
					loadMidiOutSwitchNotifier(propertyValue,
							errMidiDeviceNotAvailable);
				}

				if (oldDevice != null) {
					MidiUtils.closeAllTransmittersAndReceivers(oldDevice);
				}
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
	 * @throws MidiUnavailableException
	 *             If the midi device is not available
	 */
	private void connectMidiDeviceWithReceiver(MidiDevice device,
			Receiver receiver) throws MidiUnavailableException {

		if (device != null) {
			device.open();
			MidiUtils.setReceiverToDevice(device, receiver);
			MidiUtils.setReceiverToDevice(device, midiINDetectorReceiver);
		} else {
			throw new MidiUnavailableException();
		}
	}

	@Override
	public boolean isInMidiLearnMode() {
		return midiLearn;
	}

	@Override
	public void setMidiLearnMode(boolean midiLearn, JComponent learningComponent) {
		this.midiLearn = midiLearn;
		this.learningComponent = learningComponent;

		if (midiLearn) {
			PROGRAM_FRAME.midiLearnOn(learningComponent);
		} else {
			PROGRAM_FRAME.midiLearnOff();
		}
	}

	@Override
	public void setMidiSignature(String signature) {
		setMidiSignature(signature, learningComponent);
	}

	@Override
	public void setMidiSignature(String signature, Component component) {

		// check for unique signature
		if (midiSignatureIsAlreadyStored(signature)) {
			return;
		}

		// learning for file list
		if (component instanceof JList) {
			JList<?> list = (JList<?>) component;
			MODEL.setMidiSignature(signature, list.getSelectedIndex());
		}

		// learning for switch buttons
		if (component instanceof JButton) {
			JButton button = (JButton) component;

			if (button.getName().equals(MainFrame.NAME_PREV_BUTTON)) {
				PROPERTIES.setProperty(
						MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE,
						signature);
			}

			if (button.getName().equals(MainFrame.NAME_NEXT_BUTTON)) {
				PROPERTIES.setProperty(
						MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE,
						signature);
			}

			storePropertiesFile();
			reloadProperties();
		}

		// learning for automation list
		if (component.getName().equals(
				GUIAutomationConfigurationPanel.NAME_CONFIGURATION_TABLE)) {
			// int automationNo = PROGRAM_FRAME.getPreferencesFrame()
			// .getGuiAutomationPanel().getConfigurationTable()
			// .getSelectedRow();
			//
			// guiAutomations[automationNo].setMidiSignature(signature);
			PROGRAM_FRAME.setMidiSignature(signature, component);
		}
	}

	@Override
	public boolean isInDebugMode() {
		return DEBUG;
	}

	@Override
	public void executeMidiMessage(MidiMessage message) {

		// do not execute while midi learning
		if (!isInMidiLearnMode()) {
			String signature = MidiUtils.messageToString(message);

			// open file from list
			int index = MODEL.getMidiSignatures().indexOf(signature);

			if (index > -1) {
				openFileByIndex(index, true);
				return;
			}

			// switch file
			String prevSignature = getMidiSignature(IApplication.SWITCH_DIRECTION_PREV);
			String nextSignature = getMidiSignature(IApplication.SWITCH_DIRECTION_NEXT);

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

				if (shortMessage.getCommand() == IApplication.OPEN_FILE_MIDI_COMMAND
						&& (shortMessage.getChannel() + 1) == IApplication.OPEN_FILE_MIDI_CHANNEL
						&& shortMessage.getData1() == IApplication.OPEN_FILE_MIDI_CONTROL_NO) {

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

	@Override
	public void openPreviousFile() {

		int current = MODEL.getCurrent() - 1;
		if (current < 0) {
			current = (MODEL.getFilePaths().size() - 1);
		}
		openFileByIndex(current, true);
	}

	@Override
	public void openNextFile() {

		int current = MODEL.getCurrent() + 1;
		if (current >= MODEL.getFilePaths().size()) {
			current = 0;
		}
		openFileByIndex(current, true);
	}

	@Override
	public void openFileByIndex(int index, boolean send) {

		List<String> filePaths = null;

		filePaths = MODEL.getFilePaths();

		if (!filePaths.isEmpty()) {
			String entryName = MODEL.getEntryNames().get(index);
			String fileName = SystemUtils.replaceSystemVariables(filePaths
					.get(index));

			try {

				removeInfoMessage(infoEntryOpened);
				removeInfoMessage(errMidoFileNotFound);
				removeInfoMessage(errMidoFileCouldNotBeOpened);
				infoEntryOpened = String.format(Messages.MSG_OPENING_ENTRY,
						entryName);

				PROGRAM_FRAME.setSelectedIndex(index);
				MODEL.setCurrent(index);

				// Send MIDI change notifier
				sendItemChangeNotifier(midiOUTSwitchNotifierDevice);

				// activate per change triggered automations
				for (GUIAutomator guiAutomator : guiAutomators) {
					guiAutomator.activateOncePerChangeAutomations();
				}

				// Send MIDI remote open command
				if (send) {
					new SendItemChangeToSlavesThread(index).start();
				}

				// wait a little before opening file...
				Thread.sleep(IApplication.WAIT_BEFORE_OPENING);

				String path = resources.generateRelativeLoadingPath(fileName);
				FileUtils.openFileFromPath(path);
				setInfoMessage(infoEntryOpened);

			} catch (IllegalArgumentException ex) {
				errMidoFileNotFound = String.format(
						Messages.MSG_FILE_NOT_FOUND, fileName);
				setInfoMessage(errMidoFileNotFound);
			} catch (IOException ex) {
				errMidoFileCouldNotBeOpened = String.format(
						Messages.MSG_FILE_COULD_NOT_BE_OPENED, fileName);
				setInfoMessage(errMidoFileCouldNotBeOpened);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sends a midi message with the current index.
	 * 
	 * @param index
	 *            The index
	 */
	private void sendItemChangeToSlaves(int index) {

		String deviceName = getMidiOUTRemoteDeviceName();
		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, deviceName);

		if (deviceName != null) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {

				try {
					ShortMessage message = new ShortMessage();
					message.setMessage(IApplication.OPEN_FILE_MIDI_COMMAND,
							IApplication.OPEN_FILE_MIDI_CHANNEL - 1,
							IApplication.OPEN_FILE_MIDI_CONTROL_NO, index);

					long timeStamp = midiOUTRemoteDevice
							.getMicrosecondPosition();
					midiOUTRemoteDevice.getReceiver().send(message, timeStamp);
					showMidiOUTSignal();

					if (isInDebugMode()) {
						System.out.println(this.getClass().getName()
								+ " send: " + timeStamp + " "
								+ MidiUtils.messageToString(message));
					}

					removeInfoMessage(errMidiDeviceNotAvailable);

				} catch (MidiUnavailableException e) {

					setInfoMessage(errMidiDeviceNotAvailable);
					errMidiOUTRemoteDeviceUnavailable = errMidiDeviceNotAvailable;

				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void sendItemChangeNotifier(MidiDevice device) {

		if (device != null) {

			String errMidiDeviceNotAvailable = String.format(
					Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, device
							.getDeviceInfo().getName());
			try {
				ShortMessage message = new ShortMessage();
				message.setMessage(IApplication.SWITCH_NOTIFIER_MIDI_COMMAND,
						IApplication.SWITCH_NOTIFIER_MIDI_CHANNEL - 1,
						IApplication.SWITCH_NOTIFIER_MIDI_CONTROL_NO,
						IApplication.SWITCH_NOTIFIER_MIDI_VALUE);

				long timeStamp = device.getMicrosecondPosition();
				device.open();
				device.getReceiver().send(message, timeStamp);
				showMidiOUTSignal();

				if (isInDebugMode()) {
					System.out.println(this.getClass().getName() + " send: "
							+ timeStamp + " "
							+ MidiUtils.messageToString(message));
				}

				removeInfoMessage(errMidiDeviceNotAvailable);

			} catch (MidiUnavailableException e) {

				setInfoMessage(errMidiDeviceNotAvailable);
				errMidiOUTSwitchNotifierDeviceUnavailable = errMidiDeviceNotAvailable;

			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getMidiSignature(String switchDirection) {

		if (switchDirection.equals(IApplication.SWITCH_DIRECTION_PREV)) {
			return PROPERTIES
					.getProperty(MidiAutomatorProperties.KEY_PREV_MIDI_SIGNATURE);
		}

		if (switchDirection.equals(IApplication.SWITCH_DIRECTION_NEXT)) {
			return PROPERTIES
					.getProperty(MidiAutomatorProperties.KEY_NEXT_MIDI_SIGNATURE);
		}

		return null;
	}

	@Override
	public String getMidiSignature(int index) {
		try {
			return MODEL.getMidiSignatures().get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public String getEntryNameByIndex(int index) {
		try {
			return MODEL.getEntryNames().get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public String getEntryPathByIndex(int index) {
		try {
			return MODEL.getFilePaths().get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public String getMidiINRemoteDeviceName() {
		loadPropertiesFile();
		return (String) PROPERTIES
				.get(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
	}

	@Override
	public void setMidiINRemoteDeviceName(String midiINDeviceName) {
		oldMidiINRemoteDeviceSignature = PROPERTIES
				.getProperty(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
		PROPERTIES.setProperty(
				MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE,
				midiINDeviceName);
		storePropertiesFile();
	}

	@Override
	public String getMidiINMetronomDeviceName() {
		loadPropertiesFile();
		return (String) PROPERTIES
				.get(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE);
	}

	@Override
	public void setMidiINMetronomDeviceName(String midiINDeviceName) {
		oldMidiINMetronomDeviceSignature = PROPERTIES
				.getProperty(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE);
		PROPERTIES.setProperty(
				MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE,
				midiINDeviceName);
		storePropertiesFile();
	}

	@Override
	public String getMidiOUTRemoteDeviceName() {
		loadPropertiesFile();
		return (String) PROPERTIES
				.get(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
	}

	@Override
	public void setMidiOUTRemoteDeviceName(String deviceName) {
		oldMidiOUTRemoteDeviceSignature = PROPERTIES
				.getProperty(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
		PROPERTIES.setProperty(
				MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE, deviceName);
		storePropertiesFile();
	}

	@Override
	public String getMidiOUTSwitchNotifierDeviceName() {
		loadPropertiesFile();
		return (String) PROPERTIES
				.get(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);
	}

	@Override
	public void setMidiOUTSwitchNotifierDeviceName(String deviceName) {
		oldMidiOUTSwitchNotifierDeviceSignature = PROPERTIES
				.getProperty(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);
		PROPERTIES.setProperty(
				MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE,
				deviceName);
		storePropertiesFile();
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

		removeInfoMessage(errDuplicateMidiSignature);

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

		if (MODEL.getMidiSignatures().contains(signature)) {
			found = true;
		}

		if (signature.contains(IApplication.OPEN_FILE_MIDI_SIGNATURE)) {
			found = true;
		}

		if (found == true) {
			errDuplicateMidiSignature = String.format(
					Messages.MSG_DUPLICATE_MIDI_SIGNATURE, signature);
			setInfoMessage(errDuplicateMidiSignature);
		}

		return found;
	}

	@Override
	public boolean isInDevelopmentMode() {
		return DEV;
	}

	@Override
	public void setInfoMessage(String message) {
		if (!infoMessages.contains(message)) {
			infoMessages.add(message);
		}
		PROGRAM_FRAME.setInfoText(messagesToString(infoMessages));

	}

	@Override
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

	@Override
	public void showMidiINSignal() {
		PROGRAM_FRAME.flashMidiINDetect();
	}

	@Override
	public void showMidiOUTSignal() {
		PROGRAM_FRAME.flashMidiOUTDetect();
	}

	@Override
	public void close() {

	}

	@Override
	public Resources getResources() {
		return resources;
	}

	@Override
	public void moveUpItem(int index) {
		MODEL.exchangeIndexes(index, index - 1);
		reloadModel();
	}

	@Override
	public void moveDownItem(int index) {
		MODEL.exchangeIndexes(index, index + 1);
		reloadModel();
	}

	@Override
	public void deleteItem(int index) {
		MODEL.deleteEntry(index);
		reloadModel();
	}

	@Override
	public void addItem(String entryName, String filePath) {
		setItem(null, entryName, filePath, "");
	}

	@Override
	public void setItem(Integer index, String entryName, String filePath,
			String midiSignature) {
		removeInfoMessage(errFileCouldNotBeAdded);

		if (index == null && MODEL.getEntryNames().size() >= 128) {
			errFileCouldNotBeAdded = String.format(
					Messages.MSG_FILE_LIST_IS_FULL, entryName);
			setInfoMessage(errFileCouldNotBeAdded);
			return;
		}

		if (entryName != null && !entryName.equals("")) {
			MODEL.setEntry(index, entryName, filePath, midiSignature);
		}
		reloadModel();
	}

	@Override
	public void metronomClick(int beat) {
		if (beat == 1) {
			PROGRAM_FRAME.flashFileList(Color.RED);
		} else {
			PROGRAM_FRAME.flashFileList(Color.GREEN);
		}
	}

	@Override
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

	@Override
	public GUIAutomation[] getGUIAutomations() {
		return guiAutomations;
	}

	@Override
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

	@Override
	public void setGUIAutomationsToActive(boolean active) {
		for (GUIAutomator guiAutomator : guiAutomators) {
			guiAutomator.setActive(active);
		}
	}

	/**
	 * Tries to load the properties file.
	 */
	private void loadPropertiesFile() {
		try {
			removeInfoMessage(errPropertiesFileNotFound);
			removeInfoMessage(errPropertiesFileCouldNotBeOpened);
			PROPERTIES.load();
		} catch (FileNotFoundException e) {
			errPropertiesFileNotFound = String.format(
					Messages.MSG_FILE_NOT_FOUND, resources.getPropertiesPath()
							+ PROPERTIES_FILE_NAME);
			setInfoMessage(errPropertiesFileNotFound);

		} catch (IOException e) {
			errPropertiesFileCouldNotBeOpened = String.format(
					Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					resources.getPropertiesPath() + PROPERTIES_FILE_NAME);
			setInfoMessage(errPropertiesFileCouldNotBeOpened);
		}
	}

	/**
	 * Tries to store the properties file.
	 */
	private void storePropertiesFile() {
		try {
			removeInfoMessage(errPropertiesFileCouldNotBeOpened);
			PROPERTIES.store();

		} catch (IOException e) {
			errPropertiesFileCouldNotBeOpened = String.format(
					Messages.MSG_FILE_COULD_NOT_BE_OPENED,
					resources.getPropertiesPath() + PROPERTIES_FILE_NAME);
			setInfoMessage(errPropertiesFileCouldNotBeOpened);
		}
	}

	@Override
	public boolean isDoNotExecuteMidiMessage() {
		return doNotExecuteMidiMessage;
	}

	@Override
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
				Thread.sleep(IApplication.WAIT_BEFORE_SLAVE_SEND);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendItemChangeToSlaves(index);
		}
	}
}
