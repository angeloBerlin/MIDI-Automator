package com.midi_automator.presenter.services;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.Messages;
import com.midi_automator.utils.MidiUtils;

/**
 * Handles all MIDI master/slave actions
 * 
 * @author aguelle
 *
 */
@Service
public class MidiRemoteOpenService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	private final long WAIT_BEFORE_SLAVE_SEND = 2000;

	public static final int OPEN_FILE_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int OPEN_FILE_MIDI_CHANNEL = 1;
	public static final int OPEN_FILE_MIDI_CONTROL_NO = 102;
	public static final String OPEN_FILE_MIDI_SIGNATURE = "channel "
			+ OPEN_FILE_MIDI_CHANNEL + ": CONTROL CHANGE "
			+ OPEN_FILE_MIDI_CONTROL_NO;

	@Autowired
	private Model model;

	@Autowired
	private MidiAutomatorProperties properties;

	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiLearnService midiLearnService;
	@Autowired
	private ItemListService fileListService;
	@Autowired
	private InfoMessagesService infoMessagesService;

	/**
	 * Sends a midi message with the current index.
	 * 
	 * @param index
	 *            The index
	 */
	private void sendItemChangeToSlaves(int index) {

		String deviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
		String errMidiDeviceNotAvailable = String.format(
				Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, deviceName);

		if (deviceName != null) {
			if (!deviceName.equals(MidiAutomatorProperties.VALUE_NULL)) {

				try {
					ShortMessage message = new ShortMessage();
					message.setMessage(OPEN_FILE_MIDI_COMMAND,
							OPEN_FILE_MIDI_CHANNEL - 1,
							OPEN_FILE_MIDI_CONTROL_NO, index);

					long timeStamp = midiService.getMidiDeviceByKey(
							MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)
							.getMicrosecondPosition();
					midiService
							.getMidiDeviceByKey(
									MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE)
							.getReceiver().send(message, timeStamp);
					midiService.showMidiOUTSignal();

					log.debug("Send MIDI message: "
							+ MidiUtils.messageToString(message));

					infoMessagesService
							.removeInfoMessage(errMidiDeviceNotAvailable);

				} catch (MidiUnavailableException e) {

					log.error(errMidiDeviceNotAvailable, e);

					infoMessagesService
							.setInfoMessage(errMidiDeviceNotAvailable);
					Messages.put(
							Messages.KEY_MIDI_OUT_REMOTE_DEVICE_UNAVAILABLE,
							errMidiDeviceNotAvailable);

				} catch (InvalidMidiDataException e) {
					log.error("Sending invalid MIDI message to slaves", e);
				}
			}
		}
	}

	/**
	 * Sends the midi remote open message for the specified index
	 * 
	 * @param index
	 *            The index to open
	 */
	public void sendRemoteOpenMidiMessage(int index) {
		new SendItemChangeToSlavesThread(index).start();
	}

	/**
	 * Opens a file by midi message
	 * 
	 * @param message
	 *            The midi message
	 */
	public void openFileByMasterMidiMessage(MidiMessage message) {

		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;

			if (shortMessage.getCommand() == OPEN_FILE_MIDI_COMMAND
					&& (shortMessage.getChannel() + 1) == OPEN_FILE_MIDI_CHANNEL
					&& shortMessage.getData1() == OPEN_FILE_MIDI_CONTROL_NO) {

				int index = shortMessage.getData2();
				fileListService.selectEntryByIndex(index, false);
			}
		}
	}

	/**
	 * Opens a file if it has a learned midi message
	 * 
	 * @param message
	 *            The learned midi message
	 */
	public void openFileByLearnedMidiMessage(MidiMessage message) {

		String signature = MidiUtils.messageToString(message);

		int index = -1;
		index = model.getSetList().getMidiListeningSignatures()
				.indexOf(signature);
		if (index > -1) {
			fileListService.selectEntryByIndex(index, true);
			return;
		}
	}

	/**
	 * Opens the previous file in the list by the learned midi message
	 * 
	 * @param message
	 *            The learned midi message
	 */
	public void openPrevFileByLearnedMidiMessage(MidiMessage message) {

		String signature = MidiUtils.messageToString(message);

		String prevSignature = midiLearnService
				.getPreviousButtonMidiListeningSignature();

		if (prevSignature != null) {
			if (prevSignature.equals(signature)) {
				fileListService.openPreviousFile();
			}
		}
	}

	/**
	 * Opens the next file in the list by the learned midi message
	 * 
	 * @param message
	 *            The learned midi message
	 */
	public void openNextFileByLearnedMidiMessage(MidiMessage message) {

		String signature = MidiUtils.messageToString(message);

		String nextSignature = midiLearnService
				.getNextButtonMidiListeningSignature();

		if (nextSignature != null) {
			if (nextSignature.equals(signature)) {
				fileListService.openNextFile();
			}
		}
	}

	/**
	 * Loads the properties for the service.
	 */
	public void loadProperties() {
		midiService
				.loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);
		midiService
				.loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_REMOTE_DEVICE);
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
}
