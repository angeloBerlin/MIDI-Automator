package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiItemChangeNotificationService;

/**
 * Sends the midi signature of the current selected item.
 * 
 * @author aguelle
 * 
 */
@Component
public class SendItemMidiSignatureAction extends
		AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		midiNotificationService.sendItemSignature(mainFrame.getFileList()
				.getSelectedIndex());
	}
}
