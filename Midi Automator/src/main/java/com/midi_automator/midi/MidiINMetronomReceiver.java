package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiMetronomService;
import com.midi_automator.utils.MidiUtils;

/**
 * Displays received midi signals as metronom.
 * 
 * @author aguelle
 * 
 */
@Component
@Scope("prototype")
public class MidiINMetronomReceiver extends MidiAutomatorReceiver {

	@Autowired
	private MidiMetronomService midiMetronomService;

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiLearnService.isMidiLearning() && message != null) {
			String signature = MidiUtils.messageToString(message);

			if (signature
					.contains(MidiMetronomService.METRONOM_FIRST_CLICK_MIDI_SIGNATURE)) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						midiMetronomService.metronomClick(1);
					}
				});

			} else if (signature
					.contains(MidiMetronomService.METRONOM_CLICK_MIDI_SIGNATURE)) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						midiMetronomService.metronomClick(-1);
					}
				});
			}

		}
	}
}
