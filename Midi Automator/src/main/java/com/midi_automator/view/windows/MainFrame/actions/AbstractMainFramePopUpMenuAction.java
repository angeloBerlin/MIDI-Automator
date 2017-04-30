package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.tray.menus.TrayPopupMenu;
import com.midi_automator.view.windows.MainFrame.MainFrame;

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
	@Autowired
	protected TrayPopupMenu trayPopupMenu;

	@Override
	public void actionPerformed(ActionEvent e) {
		mainFrame.setPopupWasShown(false);
		trayPopupMenu.setVisible(false);
	}

}
