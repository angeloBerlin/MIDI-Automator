package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi_automator.presenter.MidiAutomator;

/**
 * Shows midi out signals
 * 
 * @author aguelle
 * 
 */
public class MidiOUTDetector extends MidiAutomatorReceiver {

	public MidiOUTDetector(MidiAutomator appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		application.showMidiOUTSignal();
	}
}
