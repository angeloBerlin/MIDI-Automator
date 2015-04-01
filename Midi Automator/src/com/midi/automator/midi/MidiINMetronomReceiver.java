package com.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi.automator.utils.MidiUtils;

import de.tieffrequent.midi.automator.IApplication;

/**
 * Displays received midi signals as metronom.
 * 
 * @author aguelle
 * 
 */
public class MidiINMetronomReceiver extends MidiAutomatorReceiver {

	public MidiINMetronomReceiver(IApplication appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!application.isInMidiLearnMode() && message != null) {
			String signature = MidiUtils.messageToString(message);

			if (signature
					.contains(IApplication.METRONOM_FIRST_CLICK_MIDI_SIGNATURE)) {
				application.metronomClick(1);
			} else if (signature
					.contains(IApplication.METRONOM_CLICK_MIDI_SIGNATURE)) {
				application.metronomClick(-1);
			}
		}
	}
}
