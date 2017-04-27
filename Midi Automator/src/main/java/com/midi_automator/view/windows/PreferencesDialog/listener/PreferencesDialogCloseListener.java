package com.midi_automator.view.windows.PreferencesDialog.listener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.GUIAutomationsService;

/**
 * All actions that shall be done when window is closed
 * 
 * @author aguelle
 * 
 */
@Component
public class PreferencesDialogCloseListener extends WindowAdapter {

	@Autowired
	private GUIAutomationsService guiAutomationsService;

	@Override
	public void windowClosing(WindowEvent e) {
		guiAutomationsService.setGUIAutomatorsToActive(true);
	}
}
