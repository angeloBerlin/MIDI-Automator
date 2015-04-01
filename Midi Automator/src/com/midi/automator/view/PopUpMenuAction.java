package com.midi.automator.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.midi.automator.view.frames.MainFrame;

/**
 * Common super class for popup menu Actions on the main frame
 * 
 * @author aguelle
 * 
 */
public class PopUpMenuAction extends AbstractAction {

	public PopUpMenuAction(MainFrame mainFrame) {
		super();
		parentFrame = mainFrame;
	}

	private static final long serialVersionUID = 1L;
	protected MainFrame parentFrame;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (parentFrame != null) {
			parentFrame.setPopupWasShown(false);
		}
	}

}
