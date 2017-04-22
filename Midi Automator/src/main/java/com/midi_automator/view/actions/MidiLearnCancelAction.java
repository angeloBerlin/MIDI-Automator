package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiLearnService;

/**
 * Cancels the midi learn mode and put the application to the normal state.
 * 
 * @author aguelle
 * 
 */
@Component
public class MidiLearnCancelAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiLearnService midiLearnService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		midiLearnService.setMidiLearnMode(false);
	}
}
