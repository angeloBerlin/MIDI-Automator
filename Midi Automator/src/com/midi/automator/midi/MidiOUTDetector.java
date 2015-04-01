package com.midi.automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi.automator.IApplication;

/**
 * Shows midi out signals
 * 
 * @author aguelle
 * 
 */
public class MidiOUTDetector extends MidiAutomatorReceiver {

	public MidiOUTDetector(IApplication appl) {
		super(appl);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		application.showMidiOUTSignal();
	}
}
