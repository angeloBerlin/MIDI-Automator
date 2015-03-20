package de.tieffrequent.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import de.tieffrequent.midi.automator.IApplication;
import de.tieffrequent.midi.automator.utils.MidiUtils;

/**
 * Learns a midi signal.
 * 
 * @author aguelle
 * 
 */
public class MidiINLearnReceiver extends MidiAutomatorReceiver {

	public MidiINLearnReceiver(IApplication appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		MidiMessage interpretedMessage = interpreteMessage(message, timeStamp);
		String signature = MidiUtils.messageToString(interpretedMessage);

		if (application.isInMidiLearnMode() && signature != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)) {

			if (application.isInDebugMode()) {
				System.out.println(this.getClass().getName() + " learned: "
						+ timeStamp + " " + signature);
			}

			// learn midi signal
			application.setMidiSignature(signature);
			application.setMidiLearnMode(false, null);
			application.setDoNotExecuteMidiMessage(true);
		}

	}
}
