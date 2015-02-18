package de.tieffrequent.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import de.tieffrequent.midi.automator.IApplication;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
public class MidiINDetector extends MidiOpenerReceiver {

	public MidiINDetector(IApplication appl) {
		super(appl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		application.showMidiINSignal();
	}
}
