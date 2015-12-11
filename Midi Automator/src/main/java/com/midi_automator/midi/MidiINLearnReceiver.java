package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Learns a midi signal.
 * 
 * @author aguelle
 * 
 */
public class MidiINLearnReceiver extends MidiAutomatorReceiver {

	private static Logger log = Logger.getLogger(MidiINLearnReceiver.class
			.getName());
	private static String signature;

	public MidiINLearnReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		MidiMessage interpretedMessage = interpreteMessage(message, timeStamp);
		signature = MidiUtils.messageToString(interpretedMessage);

		if (application.isInMidiLearnMode() && signature != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)) {

			log.debug("MIDI message learned: " + signature);

			// learn midi signal
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					application.setMidiSignature(signature);
					application.setMidiLearnMode(false, null);
				}
			});

		}
	}
}
