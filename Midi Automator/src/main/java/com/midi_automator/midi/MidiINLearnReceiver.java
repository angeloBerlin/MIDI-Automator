package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi_automator.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Learns a midi signal.
 * 
 * @author aguelle
 * 
 */
public class MidiINLearnReceiver extends MidiAutomatorReceiver {

	public MidiINLearnReceiver(MidiAutomator appl) {
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
