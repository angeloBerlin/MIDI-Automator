package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.utils.GUIUtils;
import com.midi_automator.view.windows.MainFrame.actions.AbstractMainFramePopUpMenuAction;

/**
 * Deletes the learned midi signature from the component
 * 
 * @author aguelle
 * 
 */
@Component
public class MidiUnlearnAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiLearnService midiLearnService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		String invokerName = GUIUtils.findInvokerName(e);
		midiLearnService.midiUnlearn(invokerName);
	}
}
