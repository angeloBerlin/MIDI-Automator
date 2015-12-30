package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINExecuteReceiver extends MidiAutomatorReceiver {

	public MidiINExecuteReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {
			isExecuting = true;

			log.debug("Executed MIDI message: " + interpretedSignature);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					application.executeMidiMessage(interpretedMessage);
					isExecuting = false;
				}
			});

		}
	}
}
