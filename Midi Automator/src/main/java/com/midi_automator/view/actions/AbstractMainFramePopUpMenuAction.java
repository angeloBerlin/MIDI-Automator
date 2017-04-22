package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.frames.MainFrame;

/**
 * Common super class for popup menu actions on the main frame
 * 
 * @author aguelle
 * 
 */
@Component
public abstract class AbstractMainFramePopUpMenuAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected MainFrame mainFrame;

	@Override
	public void actionPerformed(ActionEvent e) {
		mainFrame.setPopupWasShown(false);
	}

}
