package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

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
		super.send(message, timeStamp);

		if (!isExecuting) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					application.showMidiINSignal();
					isExecuting = false;
				}
			});
		}
	}
}
