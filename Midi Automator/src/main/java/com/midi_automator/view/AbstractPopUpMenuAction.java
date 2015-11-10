package com.midi_automator.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.midi_automator.view.frames.MainFrame;

/**
 * Common super class for popup menu actions on the main frame
 * 
 * @author aguelle
 * 
 */
public abstract class AbstractPopUpMenuAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	protected MainFrame mainFrame;

	public AbstractPopUpMenuAction(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainFrame.setPopupWasShown(false);
	}

}
