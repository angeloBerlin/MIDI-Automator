package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.midi_automator.presenter.MidiAutomator;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
public class MidiINDetector extends MidiAutomatorReceiver {

	static Logger log = Logger.getLogger(MidiINDetector.class.getName());

	public MidiINDetector(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				application.showMidiINSignal();
			}
		});
	}
}
