package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes automations by received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINAutomationReceiver extends MidiAutomatorReceiver {

	public MidiINAutomationReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {

			log.debug(name + " checking MIDI trigger for message: "
					+ interpretedSignature);

			application.activateAutomationsByMidiMessage(interpretedMessage);
			isExecuting = false;
		}
	}
}
