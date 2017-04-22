package com.midi_automator.view.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.tray.Tray;

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
	private Tray tray;

	public static final String MENU_ITEM_OPEN_MIDI_AUTOMATOR = "Open...";
	public static final String MENU_ITEM_HIDE_MIDI_AUTOMATOR = "Hide...";

	@Override
	public void actionPerformed(ActionEvent e) {

		if (programFrame.isVisible()) {
			programFrame.setState(Frame.ICONIFIED);
			programFrame.setVisible(false);
			tray.getHideShowMenuItem().setLabel(MENU_ITEM_OPEN_MIDI_AUTOMATOR);
		} else {
			programFrame.setState(Frame.NORMAL);
			programFrame.setVisible(true);
			tray.getHideShowMenuItem().setLabel(MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		}
	}
}
