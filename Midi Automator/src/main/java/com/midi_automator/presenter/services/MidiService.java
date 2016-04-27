package com.midi_automator.presenter.services;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.midi_automator.midi.MidiINAutomationReceiver;
import com.midi_automator.midi.MidiINDetector;
import com.midi_automator.midi.MidiINExecuteReceiver;
import com.midi_automator.midi.MidiINLearnReceiver;
import com.midi_automator.midi.MidiINMetronomReceiver;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Messages;
import com.midi_automator.presenter.Presenter;
import com.midi_automator.utils.MidiUtils;
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
	private ApplicationContext ctx;

	@Autowired
	private MidiAutomatorProperties properties;
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
	private InfoMessagesService infoMessagesService;
	@Autowired
	private MidiLearnService midiLearnService;

	private Map<String, MidiDevice> midiDevices = new HashMap<String, MidiDevice>();
	private Map<String, Set<Receiver>> midiFunctionReceiverMapping = new HashMap<String, Set<Receiver>>();

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

		if (midiDeviceName != null && !midiDeviceName.equals("")) {

			// MIDI IN Remote
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE)) {
				loadMidiInRemoteDevice(midiDeviceName);
				return;
			}

			// MIDI IN Automation Trigger
			if (functionKey
					.contains(MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE)) {
				loadMidiInAutomationTriggerDevice(midiDeviceName, functionKey);
				return;
			}

			// MIDI IN Metronom
			if (functionKey
					.equals(MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE)) {
				loadMidiInMetronomDevice(midiDeviceName);
				return;
			}

			// MIDI OUT
			reloadMidiDevice(midiDeviceName, functionKey, null, "OUT");
		}
	}

	/**
	 * Loads the midi in remote device
	 * 
	 * @param midiDeviceName
	 *            The midi device name
	 */
	private void loadMidiInRemoteDevice(String midiDeviceName) {

		Set<Receiver> receivers = new HashSet<Receiver>();
		receivers.add(ctx.getBean(MidiINLearnReceiver.class));
		receivers.add(ctx.getBean(MidiINExecuteReceiver.class));

		reloadMidiDevice(midiDeviceName,
				MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE, receivers,
				"IN");
	}

	/**
	 * Loads the midi in automation trigger device device
	 * 
	 * @param midiDeviceName
	 *            The midi device name
	 * @param functionKey
	 *            The function key of the trigger
	 */
	private void loadMidiInAutomationTriggerDevice(String midiDeviceName,
			String functionKey) {

		Set<Receiver> receivers = new HashSet<Receiver>();
		receivers.add(ctx.getBean(MidiINLearnReceiver.class));
		MidiINAutomationReceiver automationReceiver = ctx
				.getBean(MidiINAutomationReceiver.class);
		automationReceiver.setName(functionKey);
		receivers.add(automationReceiver);

		reloadMidiDevice(midiDeviceName, functionKey, receivers, "IN");
	}

	/**
	 * Loads the midi in metronom device
	 * 
	 * @param midiDeviceName
	 *            The midi device name
	 */
	private void loadMidiInMetronomDevice(String midiDeviceName) {

		Set<Receiver> receivers = new HashSet<Receiver>();
		receivers.add(ctx.getBean(MidiINMetronomReceiver.class));

		reloadMidiDevice(midiDeviceName,
				MidiAutomatorProperties.KEY_MIDI_IN_METRONOM_DEVICE, receivers,
				"IN");
	}

	/**
	 * Loads/Reloads a MIDI device
	 * 
	 * @param midiDeviceName
	 *            The name of the midi device
	 * @param functionKey
	 *            The key name of the midi function
	 * @param receivers
	 *            A set of midi receivers for the device
	 * @param direction
	 *            The direction "IN"/"OUT" of the midi device
	 */
	private void reloadMidiDevice(String midiDeviceName, String functionKey,
			Set<Receiver> receivers, String direction) {

		// get old MidiDevice
		MidiDevice oldMidiDevice = midiDevices.get(functionKey);
		String oldDeviceName = null;

		if (oldMidiDevice != null) {
			oldDeviceName = oldMidiDevice.getDeviceInfo().getName();
		}

		if (!midiDeviceName.equals(oldDeviceName)) {

			// unregister old function
			midiDevices.put(functionKey, null);

			// load new device
			if (!midiDeviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {

				loadMidiDevice(midiDeviceName, functionKey, receivers,
						direction);
			} else {
				midiDevices.remove(functionKey);
			}

			// unload old device
			unloadMidiDevice(oldDeviceName, functionKey, direction);
		}
	}

	/**
	 * Loads a MIDI device
	 * 
	 * @param midiDeviceName
	 *            The name of the midi device
	 * @param functionKey
	 *            The key name of the midi function
	 * @param receivers
	 *            A set of midi receivers for the device
	 * @param direction
	 *            The direction "IN"/"OUT" of the midi device
	 */
	private void loadMidiDevice(String midiDeviceName, String functionKey,
			Set<Receiver> receivers, String direction) {

		String loadingErrorMessage = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, midiDeviceName);

		Messages.put(functionKey + "_UNVAILABLE", loadingErrorMessage);

		try {
			MidiDevice device = MidiUtils.getMidiDevice(midiDeviceName,
					direction);

			if (!device.isOpen()) {
				device.open();
				log.info("Opened MIDI " + direction + " device "
						+ device.getDeviceInfo().getName());
			}

			if (receivers != null) {
				for (Receiver receiver : receivers) {
					connectMidiDeviceWithReceiver(device, receiver, direction);
					midiFunctionReceiverMapping.put(functionKey, receivers);
				}
			}

			// register new device for function
			midiDevices.put(functionKey, device);

			infoMessagesService.removeInfoMessage(loadingErrorMessage);

		} catch (MidiUnavailableException e) {
			infoMessagesService.setInfoMessage(loadingErrorMessage);
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
				Receiver midiINDetector = ctx.getBean(MidiINDetector.class);

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
	 * Runs the function for the midi message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void executeMidiMessage(MidiMessage message) {

		if (!midiLearnService.isMidiLearning()) {

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
	 * Sends a midi signature to a given device
	 * 
	 * @param device
	 *            The midi device
	 * @param midiSignature
	 *            The midi signature
	 */
	public void sendMidiMessage(MidiDevice device, MidiMessage message)
			throws MidiUnavailableException {

		if (message == null) {
			return;
		}

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
}
