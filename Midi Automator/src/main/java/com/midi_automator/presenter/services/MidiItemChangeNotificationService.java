package com.midi_automator.presenter.services;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.Messages;
import com.midi_automator.utils.MidiUtils;

/**
 * Handles all midi out communications when an item changes.
 * 
 * @author aguelle
 *
 */
@Service
public class MidiItemChangeNotificationService {

	private Logger log = Logger.getLogger(this.getClass().getName());

	public static final int SWITCH_NOTIFIER_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int SWITCH_NOTIFIER_MIDI_CHANNEL = 1;
	public static final int SWITCH_NOTIFIER_MIDI_CONTROL_NO = 103;
	public static final int SWITCH_NOTIFIER_MIDI_VALUE = 127;
	public static final String SWITCH_NOTIFIER_MIDI_SIGNATURE = "channel "
			+ SWITCH_NOTIFIER_MIDI_CHANNEL + ": CONTROL CHANGE "
			+ SWITCH_NOTIFIER_MIDI_CONTROL_NO;

	public static final int ITEM_SEND_MIDI_COMMAND = ShortMessage.CONTROL_CHANGE;
	public static final int ITEM_SEND_MIDI_CHANNEL = 16;
	public static final int ITEM_SEND_MIDI_VALUE = 127;

	@Autowired
	private MidiService midiService;
	@Autowired
	private FileListService fileListService;
	@Autowired
	private InfoMessagesService infoMessagesService;

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

				midiService.sendMidiMessage(device, message);
				infoMessagesService
						.removeInfoMessage(errMidiDeviceNotAvailable);

			} catch (MidiUnavailableException e) {

				infoMessagesService.setInfoMessage(errMidiDeviceNotAvailable);
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
	 * Sends the midi message of the item.
	 * 
	 * @param device
	 *            The midi item device
	 * @param signature
	 *            The item's midi signature
	 */
	public void sendItemSignature(MidiDevice device, String signature) {

		if (device != null) {

			String errMidiDeviceNotAvailable = String.format(
					Messages.MSG_MIDI_DEVICE_NOT_AVAILABLE, device
							.getDeviceInfo().getName());
			try {
				MidiMessage message = MidiUtils.signatureToMessage(signature);

				log.info("Sending MIDI signature of item: " + signature);
				midiService.sendMidiMessage(device, message);
				infoMessagesService
						.removeInfoMessage(errMidiDeviceNotAvailable);

			} catch (MidiUnavailableException e) {

				infoMessagesService.setInfoMessage(errMidiDeviceNotAvailable);
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
	 * Sends the midi signature of a specific item from the list.
	 * 
	 * @param index
	 *            The index number of the item in the list
	 */
	public void sendItemSignature(int index) {
		sendItemSignature(
				midiService
						.getMidiDeviceByKey(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE),
				fileListService.getMidiFileListSendingSignature(index));
	}

	/**
	 * Loads the properties for the service.
	 */
	public void loadProperties() {

		midiService
				.loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_NOTIFIER_DEVICE);
		midiService
				.loadMidiDeviceProperty(MidiAutomatorProperties.KEY_MIDI_OUT_SWITCH_ITEM_DEVICE);
	}
}
