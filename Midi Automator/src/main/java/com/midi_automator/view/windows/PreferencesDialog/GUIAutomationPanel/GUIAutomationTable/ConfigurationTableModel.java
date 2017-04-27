package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable;

import javax.swing.table.DefaultTableModel;

/**
 * The model for the automation configuration table.
 * 
 * @author aguelle
 * 
 */
public class ConfigurationTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int row, int column) {

		if (column == findColumn(GUIAutomationTable.COLNAME_MIDI_SIGNATURE)) {
			return false;
		}

		if (column == findColumn(GUIAutomationTable.COLNAME_KEYS)) {
			return false;
		}

		return true;
	}
}
