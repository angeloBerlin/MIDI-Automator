package com.midi_automator.presenter.services;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.midi.MidiINAutomationReceiver;
import com.midi_automator.midi.MidiINDetector;
import com.midi_automator.midi.MidiINExecuteReceiver;
import com.midi_automator.midi.MidiINLearnReceiver;
import com.midi_automator.midi.MidiINMetronomReceiver;
import com.midi_automator.model.IModel;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Messages;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

/**
 * Handles all midi devices
 * 
 * @author aguelle
 *
 */
@Service
public class MidiService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	private IModel model;

	@Autowired
	private Presenter presenter;
	@Autowired
	private MainFrame mainFrame;

	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private MidiRemoteOpenService midiRemoteOpenService;
	@Autowired
	private MidiMetronomService midiMetronomService;
	@Autowired
	private FileListService fileListService;

	private Map<String, MidiDevice> midiDevices = new HashMap<String, MidiDevice>();
	private Map<String, Set<Receiver>> midiFunctionReceiverMapping = new HashMap<String, Set<Receiver>>();
	private boolean midiLearning;
	private JComponent learningComponent;

	/**
	 * Gets a stored midi device by key
	 * 
	 * @param key
	 *            The key for the midi device
	 * @return A MidiDevice
	 */
	public MidiDevice getMidiDeviceByKey(String key) {
		return midiDevices.get(key);
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
				receivers.add(new MidiINLearnReceiver(presenter, this));
				receivers.add(new MidiINExecuteReceiver(presenter, this));

				loadMidiDevice(midiDeviceName, functionKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI IN Automation Trigger
			if (functionKey
					.contains(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(new MidiINLearnReceiver(presenter, this));
				MidiINAutomationReceiver automationReceiver = new MidiINAutomationReceiver(
						presenter, this, guiAutomationsService);
				automationReceiver.setName(functionKey);
				receivers.add(automationReceiver);

				loadMidiDevice(midiDeviceName, functionKey, receivers, "IN",
						errMidiDeviceNotAvailable);
			}

			// MIDI IN Metronom
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE)) {

				Set<Receiver> receivers = new HashSet<Receiver>();
				receivers.add(new MidiINMetronomReceiver(presenter, this,
						midiMetronomService));

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

					presenter.removeInfoMessage(loadingErrorMessage);

				} catch (MidiUnavailableException e) {
					presenter.setInfoMessage(loadingErrorMessage);
				}
			} else {
				midiDevices.remove(functionKey);
			}

			// unload old device
			unloadMidiDevice(oldDeviceName, functionKey, direction);
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
				Receiver midiINDetector = new MidiINDetector(presenter, this);

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

				unloadMidiDevice(device, functionKey);

			} catch (MidiUnavailableException e) {
				log.error("Retrieving MIDI device failed.", e);
			}
		}
	}

	/**
	 * Unloads a midi device
	 * 
	 * @param device
	 *            The device
	 * @param functionKey
	 *            The name of the function key
	 */
	private void unloadMidiDevice(MidiDevice device, String functionKey) {

		try {

			if (device != null) {
				removeReceiversFromDevice(device,
						midiFunctionReceiverMapping.get(functionKey));

				if (!midiDevices.containsValue(device)) {
					device.close();

					log.info("Closed MIDI "
							+ MidiUtils.getDirectionOfMidiDevice(device)
							+ " device: " + device.getDeviceInfo().getName());
				}
			}

		} catch (MidiUnavailableException e) {
			log.error("Unloading MIDI device failed.", e);
		}
	}

	/**
	 * Unloads all midi devices
	 */
	public void unloadAllMidiDevices() {

		for (Map.Entry<String, MidiDevice> entry : midiDevices.entrySet()) {

			String functionKey = entry.getKey();
			MidiDevice device = entry.getValue();
			unloadMidiDevice(device, functionKey);
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
	 * Checks if the midi signature is already stored and displays a failure.
	 * 
	 * @param signature
	 *            The midi signature
	 * @return <TRUE> if it is already stored, <FALSE> if not
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public boolean isMidiSignatureAlreadyStored(String signature) {

		presenter.removeInfoMessage(Messages.builtMessages
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

		if (signature.contains(MidiRemoteOpenService.OPEN_FILE_MIDI_SIGNATURE)) {
			found = true;
		}

		if (found == true) {
			String errDuplicateMidiSignature = String.format(
					Messages.MSG_DUPLICATE_MIDI_SIGNATURE, signature);
			Messages.builtMessages.put(
					Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE,
					errDuplicateMidiSignature);
			presenter.setInfoMessage(Messages.builtMessages
					.get(Messages.KEY_ERROR_DUPLICATE_MIDI_SIGNATURE));
		}

		return found;
	}

	/**
	 * Runs the function for the midi message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void executeMidiMessage(MidiMessage message) {

		if (!midiLearning) {

			String signature = MidiUtils.messageToString(message);
			log.debug("Executed MIDI message: " + signature);

			midiRemoteOpenService.openFileByLearnedMidiMessage(message);
			midiRemoteOpenService.openFileByMasterMidiMessage(message);
			midiRemoteOpenService.openPrevFileByLearnedMidiMessage(message);
			midiRemoteOpenService.openNextFileByLearnedMidiMessage(message);

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
		presenter.storePropertiesFile();
	}

	/**
	 * Gets the name of a midi device by a properties key
	 * 
	 * @param key
	 *            The properties key
	 * @return The name of the midid device
	 */
	public String getMidiDeviceName(String key) {
		return (String) properties.get(key);
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

		if (component == null) {
			return;
		}

		// learning for file list
		if (component instanceof JList) {
			JList<?> list = (JList<?>) component;

			// check for unique signature
			if (isMidiSignatureAlreadyStored(midiSignature)) {
				return;
			}

			model.getSetList().getItems().get(list.getSelectedIndex())
					.setMidiListeningSignature(midiSignature);
			fileListService.saveSetList();
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

			presenter.storePropertiesFile();
			presenter.loadProperties();
		}

		// learning for automation list
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {
			mainFrame.setMidiSignature(midiSignature);
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
	 * Sets the application to midi learn mode
	 * 
	 * @param midiLearning
	 *            <TRUE> application is in midi learn mode, <FALSE> application
	 *            is not in midi learn mode
	 * @param learningComponent
	 *            The component for which shall be learned, may be <NULL> if
	 *            midi learn is <FALSE>
	 */
	public void setMidiLearnMode(boolean midiLearning,
			JComponent learningComponent) {
		this.midiLearning = midiLearning;
		setLearningComponent(learningComponent);

		if (midiLearning) {
			mainFrame.setInfoText(Messages.MSG_MIDI_LEARN_MODE);
			mainFrame.midiLearnOn(learningComponent);
		} else {
			mainFrame.midiLearnOff();
		}

		log.debug("Property midiLearn=" + midiLearning);
	}

	/**
	 * Sends a midi signature to a given device
	 * 
	 * @param device
	 *            The midi device
	 * @param midiSignature
	 *            The midi signature
	 */
	public void sendMidiMessage(MidiDevice device, MidiMessage message)
			throws MidiUnavailableException {

		long timeStamp = device.getMicrosecondPosition();
		device.open();
		device.getReceiver().send(message, timeStamp);
		showMidiOUTSignal();

		log.debug("Send MIDI message: " + MidiUtils.messageToString(message));
	}

	/**
	 * Indicates a midi IN signal
	 */
	public void showMidiINSignal() {
		mainFrame.blinkMidiINDetect();
	}

	/**
	 * Loads a midi device property by opening and connecting the configured
	 * midi devices.
	 * 
	 * 
	 * @param propertyKey
	 *            The property key for the midi device
	 */
	public void loadMidiDeviceProperty(String propertyKey) {

		String propertyValue = properties.getProperty(propertyKey);
		loadMidiDeviceByFunctionKey(propertyKey, propertyValue);
	}

	/**
	 * Indicates a midi OUT signal
	 */
	public void showMidiOUTSignal() {
		mainFrame.blinkMidiOUTDetect();
	}

	public boolean isMidiLearning() {
		return midiLearning;
	}

	public JComponent getLearningComponent() {
		return learningComponent;
	}

	public void setLearningComponent(JComponent learningComponent) {
		this.learningComponent = learningComponent;
	}
}
