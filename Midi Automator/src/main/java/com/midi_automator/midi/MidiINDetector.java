package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.MidiService;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
public class MidiINDetector extends MidiAutomatorReceiver {

	public MidiINDetector(Presenter presenter, MidiService midiService) {
		super(presenter, midiService);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!isExecuting) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					log.debug("Blink MIDI IN");
					midiService.showMidiINSignal();
					isExecuting = false;
				}
			});
		}
	}
}
