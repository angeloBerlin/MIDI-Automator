package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import org.apache.log4j.Logger;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINExecuteReceiver extends MidiAutomatorReceiver {

	static Logger log = Logger.getLogger(MidiINExecuteReceiver.class.getName());

	public MidiINExecuteReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		super.send(message, timeStamp);

		MidiMessage interpretedMessage = interpreteMessage(message, timeStamp);
		String signature = MidiUtils.messageToString(interpretedMessage);

		log.debug("Property doNotExecute="
				+ application.isDoNotExecuteMidiMessage());
		log.debug("Property interpretedMessage="
				+ interpretedMessage);
		
		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !application.isDoNotExecuteMidiMessage()) {

			log.debug("Interpreted MIDI message: " + signature);

			application.executeMidiMessage(interpretedMessage);
		}

		application.setDoNotExecuteMidiMessage(false);
	}
}
