package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.utils.MidiUtils;

/**
 * Displays received midi signals as metronom.
 * 
 * @author aguelle
 * 
 */
public class MidiINMetronomReceiver extends MidiAutomatorReceiver {

	public MidiINMetronomReceiver(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!application.isInMidiLearnMode() && message != null) {
			String signature = MidiUtils.messageToString(message);

			if (signature
					.contains(MidiAutomator.METRONOM_FIRST_CLICK_MIDI_SIGNATURE)) {
				application.metronomClick(1);
			} else if (signature
					.contains(MidiAutomator.METRONOM_CLICK_MIDI_SIGNATURE)) {
				application.metronomClick(-1);
			}
		}
	}
}
