package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINExecuteReceiver extends MidiAutomatorReceiver {

	public MidiINExecuteReceiver(Presenter presenter,
			MidiService midiService) {
		super(presenter, midiService);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiService.isMidiLearning() && interpretedMessage != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {
			isExecuting = true;

			log.debug("Executed MIDI message: " + interpretedSignature);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					midiService.executeMidiMessage(interpretedMessage);
					isExecuting = false;
				}
			});

		}
	}
}
