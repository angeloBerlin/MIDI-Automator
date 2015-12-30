package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import com.midi_automator.presenter.MidiAutomator;
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

		if (application.isInMidiLearnMode() && interpretedSignature != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {
			isExecuting = true;

			log.debug("MIDI message learned: " + interpretedSignature);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					application.setMidiSignature(interpretedSignature);
					application.setMidiLearnMode(false, null);
					isExecuting = false;
				}
			});

		}
	}
}
