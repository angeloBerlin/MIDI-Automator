package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingWorker;

import com.midi_automator.presenter.MidiAutomator;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
public class MidiINDetector extends MidiAutomatorReceiver {

	public MidiINDetector(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				application.showMidiINSignal();
				return null;
			}
		};
		worker.execute();

	}
}
