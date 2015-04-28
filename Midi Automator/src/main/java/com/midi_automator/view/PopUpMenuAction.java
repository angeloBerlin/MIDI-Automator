package com.midi_automator.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.midi_automator.view.frames.MainFrame;

/**
 * Common super class for popup menu Actions on the main frame
 * 
 * @author aguelle
 * 
 */
public class PopUpMenuAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	protected final MainFrame PROGRAM_FRAME;

	public PopUpMenuAction(MainFrame mainFrame) {
		super();
		PROGRAM_FRAME = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (PROGRAM_FRAME != null) {
			PROGRAM_FRAME.setPopupWasShown(false);
		}
	}

}
