package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes automations by received midi signals.
 * 
 * @author aguelle
 * 
 */
@Component
@Scope("prototype")
public class MidiINAutomationReceiver extends MidiAutomatorReceiver {

	@Autowired
	private GUIAutomationsService guiAutomationsService;

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiLearnService.isMidiLearning() && interpretedMessage != null
				&& !interpretedSignature.equals(MidiUtils.UNKNOWN_MESSAGE)
				&& !isExecuting) {

			log.debug(name + " checking MIDI trigger for message: "
					+ interpretedSignature);

			guiAutomationsService
					.activateAutomationsByMidiMessage(interpretedMessage);
			isExecuting = false;
		}
	}
}
