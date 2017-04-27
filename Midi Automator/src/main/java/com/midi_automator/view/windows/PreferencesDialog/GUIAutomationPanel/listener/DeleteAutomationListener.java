package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Deletes the chosen configuration from the configuration table.
 */
@Component
public class DeleteAutomationListener implements ActionListener {

	@Autowired
	private GUIAutomationTable table;

	@Override
	public void actionPerformed(ActionEvent e) {
		table.deleteAutomation(table.getSelectedRow());
	}
}
