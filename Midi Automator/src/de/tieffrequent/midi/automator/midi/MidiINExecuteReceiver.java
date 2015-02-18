package de.tieffrequent.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import de.tieffrequent.midi.automator.IApplication;
import de.tieffrequent.midi.automator.utils.MidiUtils;

/**
 * Executes received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINExecuteReceiver extends MidiOpenerReceiver {

	public MidiINExecuteReceiver(IApplication appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		super.send(message, timeStamp);

		MidiMessage interpretedMessage = interpreteMessage(message, timeStamp);
		String signature = MidiUtils.messageToString(interpretedMessage);

		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)) {

			if (application.isInDebugMode()) {
				System.out.println(this.getClass().getName() + " interpreted: "
						+ timeStamp + " " + signature);
			}

			application.executeMidiMessage(interpretedMessage);
		}
	}
}
