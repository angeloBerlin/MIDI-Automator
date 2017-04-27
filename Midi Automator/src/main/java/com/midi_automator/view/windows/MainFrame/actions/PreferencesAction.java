package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;

/**
 * Opens the preferences window
 * 
 * @author aguelle
 * 
 */
@Component
public class PreferencesAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MainFrame mainFrame;
	@Autowired
	private PreferencesDialog preferencesDialog;

	@Override
	public void actionPerformed(ActionEvent e) {

		// preferencesDialog = ctx.getBean("preferencesDialog",
		// PreferencesDialog.class);
		preferencesDialog.init();
		preferencesDialog.setLocation(mainFrame.getLocationOnScreen());
		preferencesDialog.showDialog();
	}
}
