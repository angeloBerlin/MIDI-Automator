package com.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi.automator.IApplication;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
public class MidiINDetector extends MidiAutomatorReceiver {

	public MidiINDetector(IApplication appl) {
		super(appl);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		application.showMidiINSignal();
	}
}
