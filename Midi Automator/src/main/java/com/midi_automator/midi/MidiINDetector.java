package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;
import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiService;

/**
 * Shows midi in signals
 * 
 * @author aguelle
 * 
 */
@Component
@Scope("prototype")
public class MidiINDetector extends MidiAutomatorReceiver {

	@Autowired
	private MidiService midiService;

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
