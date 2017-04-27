package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.windows.MainFrame.actions.AbstractMainFramePopUpMenuAction;

/**
 * Puts the application to the midi learn mode for the selected component.
 * 
 * @author aguelle
 * 
 */
@Component
public class MidiLearnAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiLearnService midiLearnService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		String invokerName = GUIUtils.findInvokerName(e);
		midiLearnService.activateMidiLearn(invokerName);
	}
}
