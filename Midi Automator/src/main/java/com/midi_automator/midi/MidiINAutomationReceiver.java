package com.midi_automator.midi;

import javax.sound.midi.MidiMessage;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.utils.MidiUtils;

/**
 * Executes automations by received midi signals.
 * 
 * @author aguelle
 * 
 */
public class MidiINAutomationReceiver extends MidiAutomatorReceiver {

	private GUIAutomationsService guiAutomationsService;

	/**
	 * Constructor
	 * 
	 * @param presenter
	 *            The main application
	 * @param midiService
	 *            The midi service
	 * @param guiAutomationsService
	 *            The GUIAutomationsService
	 */
	public MidiINAutomationReceiver(Presenter presenter,
			MidiService midiService, GUIAutomationsService guiAutomationsService) {
		super(presenter, midiService);
		this.guiAutomationsService = guiAutomationsService;
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		super.send(message, timeStamp);

		if (!midiService.isMidiLearning() && interpretedMessage != null
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
