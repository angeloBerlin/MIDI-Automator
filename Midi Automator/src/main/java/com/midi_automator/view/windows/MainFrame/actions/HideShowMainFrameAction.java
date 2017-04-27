package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.tray.menus.TrayPopupMenu;
import com.midi_automator.view.windows.MainFrame.MainFrame;

/**
 * Hides and restores the Main Frame.
 * 
 * @author aguelle
 * 
 */
@Component
public class HideShowMainFrameAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MainFrame programFrame;
	@Autowired
	private TrayPopupMenu trayPopupMenu;

	@Override
	public void actionPerformed(ActionEvent e) {

		if (programFrame.isVisible()) {
			programFrame.setState(Frame.ICONIFIED);
			programFrame.setVisible(false);
			trayPopupMenu.setHiddenState(true);
		} else {
			programFrame.setState(Frame.NORMAL);
			programFrame.setVisible(true);
			trayPopupMenu.setHiddenState(false);
		}
	}
}
