package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

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

	private static Logger log = Logger.getLogger(MidiINAutomationReceiver.class
			.getName());
	private static MidiMessage interpretedMessage;

	public MidiINAutomationReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		super.send(message, timeStamp);

		interpretedMessage = interpreteMessage(message, timeStamp);
		String signature = MidiUtils.messageToString(interpretedMessage);

		log.debug("Property doNotExecute="
				+ application.isDoNotExecuteMidiMessage());

		if (!application.isInMidiLearnMode() && interpretedMessage != null
				&& !signature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !application.isDoNotExecuteMidiMessage()) {

			log.debug(name + " interpreted MIDI message: " + signature);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					application
							.activateAutomationsByMidiMessage(interpretedMessage);
				}
			});
		}

		application.setDoNotExecuteMidiMessage(false);
	}
}
