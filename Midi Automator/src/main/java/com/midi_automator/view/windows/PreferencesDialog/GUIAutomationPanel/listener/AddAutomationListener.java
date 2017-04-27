package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Adds a new configuration to the configuration table.
 */
@Component
public class AddAutomationListener implements ActionListener {

	@Autowired
	private GUIAutomationTable table;

	@Override
	public void actionPerformed(ActionEvent e) {
		table.setAutomation(null, null, null, null, null,
				GUIAutomation.DEFAULT_MIN_DELAY, GUIAutomation.DEFAULT_TIMEOUT,
				null, GUIAutomation.DEFAULT_SCAN_RATE,
				GUIAutomation.DEFAULT_IS_MOVABLE, -1);
	}
}
