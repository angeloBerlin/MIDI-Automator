package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes received midi signals.
 * 
 * @author aguelle
 * 
 */
@Component
@Scope("prototype")
public class MidiINExecuteReceiver extends MidiAutomatorReceiver {

	@Autowired
	private MidiService midiService;

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiLearnService.isMidiLearning() && interpretedMessage != null
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
