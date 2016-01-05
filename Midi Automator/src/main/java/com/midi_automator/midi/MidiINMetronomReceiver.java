package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.MidiMetronomService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.MidiUtils;

/**
 * Displays received midi signals as metronom.
 * 
 * @author aguelle
 * 
 */
public class MidiINMetronomReceiver extends MidiAutomatorReceiver {

	private MidiMetronomService midiMetronomService;

	/**
	 * Constructor
	 * 
	 * @param presenter
	 *            The main application presenter
	 * @param midiService
	 *            The midi service
	 * @param midiMetronomService
	 *            The metronom service
	 */
	public MidiINMetronomReceiver(Presenter presenter,
			MidiService midiService, MidiMetronomService midiMetronomService) {
		super(presenter, midiService);
		this.midiMetronomService = midiMetronomService;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiService.isMidiLearning() && message != null) {
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
