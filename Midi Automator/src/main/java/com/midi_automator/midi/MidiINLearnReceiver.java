package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.midi_automator.utils.MidiUtils;

/**
 * Learns a midi signal.
 * 
 * @author aguelle
 * 
 */
@Component
@Scope("prototype")
public class MidiINLearnReceiver extends MidiAutomatorReceiver {

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (midiLearnService.isMidiLearning() && interpretedSignature != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {
			isExecuting = true;

			log.debug("MIDI message learned: " + interpretedSignature);

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					midiLearnService.midiLearn(interpretedSignature);
					midiLearnService.setMidiLearnMode(false);
					isExecuting = false;
				}
			});

		}
	}
}
