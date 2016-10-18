package com.midi_automator.presenter.services;

import java.awt.Component;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.JList;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.Messages;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.frames.MainFrame;

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
	private final String SWITCH_DIRECTION_PREV = "previous";
	private final String SWITCH_DIRECTION_NEXT = "next";

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
	private FileListService fileListService;
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

		String prevSignature = getMidiListeningSignature(SWITCH_DIRECTION_PREV);

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

		String nextSignature = getMidiListeningSignature(SWITCH_DIRECTION_NEXT);

		if (nextSignature != null) {
			if (nextSignature.equals(signature)) {
				fileListService.openNextFile();
			}
		}
	}

	/**
	 * Gets the midi listening signature of the switch directions
	 * 
	 * @param switchDirection
	 *            SWITCH_DIRECTION_PREV previous direction,
	 *            SWITCH_DIRECTION_NEXT next direction
	 * @return The midi signature
	 */
	private String getMidiListeningSignature(String switchDirection) {

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
	 * Checks if a midi signature was learned for the given component name
	 * 
	 * @param component
	 *            The component
	 * @return <TRUE> if it was learned, <FALSE> if it was not learned or
	 *         unlearned
	 */
	public boolean isMidiLearned(Component component) {
		boolean isLearned = false;

		// previous switch button
		String prevSignature = getMidiListeningSignature(SWITCH_DIRECTION_PREV);
		if (prevSignature != null
				&& !prevSignature.equals(MidiAutomatorProperties.VALUE_NULL)) {
			if (component.getName().equals(MainFrame.NAME_PREV_BUTTON)
					&& (!prevSignature.equals(""))) {
				isLearned = true;
			}
		}

		// next switch button
		String nextSignature = getMidiListeningSignature(SWITCH_DIRECTION_NEXT);
		if (nextSignature != null
				&& !nextSignature.equals(MidiAutomatorProperties.VALUE_NULL)) {
			if (component.getName().equals(MainFrame.NAME_NEXT_BUTTON)
					&& (!nextSignature.equals(""))) {
				isLearned = true;
			}
		}

		// file list item
		if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {
			if (component instanceof JList) {
				JList<?> fileList = (JList<?>) component;

				String selectedSignature = fileListService
						.getMidiFileListListeningSignature(fileList
								.getSelectedIndex());

				if (selectedSignature != null) {
					if (!selectedSignature.equals("")) {
						isLearned = true;
					}
				}
			}
		}

		return isLearned;
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
