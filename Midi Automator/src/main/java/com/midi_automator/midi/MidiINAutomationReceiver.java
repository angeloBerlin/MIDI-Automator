package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import org.apache.log4j.Logger;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes automations by received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINAutomationReceiver extends MidiAutomatorReceiver {

	static Logger log = Logger.getLogger(MidiINAutomationReceiver.class
			.getName());

	public MidiINAutomationReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		super.send(message, timeStamp);

		MidiMessage interpretedMessage = interpreteMessage(message, timeStamp);
		String signature = MidiUtils.messageToString(interpretedMessage);

		log.debug("Property doNotExecute="
				+ application.isDoNotExecuteMidiMessage());

		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !application.isDoNotExecuteMidiMessage()) {

			log.debug(name + " interpreted MIDI message: " + signature);

			application.activateAutomationsByMidiMessage(interpretedMessage);
		}

		application.setDoNotExecuteMidiMessage(false);
	}
}
