package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.listener;

import java.awt.event.MouseEvent;

import org.springframework.stereotype.Component;

import com.midi_automator.view.DeActivateableMouseAdapter;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Shows the context menu.
 * 
 * @author aguelle
 * 
 */
@Component
public class GUIAutomationTablePopupListener extends DeActivateableMouseAdapter {

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mouseReleased(e);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if (isActive()) {
				maybeShowPopup(e);
			}
		}
	}

	/**
	 * Shows the popup menu if it was a popup trigger. The trigger is OS
	 * specific.
	 * 
	 * @param e
	 *            The mouse event
	 */
	private void maybeShowPopup(MouseEvent e) {

		GUIAutomationTable table = (GUIAutomationTable) e
				.getSource();
		int row = table.rowAtPoint(e.getPoint());
		int column = table.columnAtPoint(e.getPoint());

		if (!table.isRowSelected(row)) {
			table.changeSelection(row, column, false, false);
		}

		if (column == table.getColumn(
				GUIAutomationTable.COLNAME_MIDI_SIGNATURE)
				.getModelIndex()) {
			table.openMidiLearnPopupMenu(table, e.getX(), e.getY());
		}

		if (column == table.getColumn(
				GUIAutomationTable.COLNAME_KEYS).getModelIndex()) {
			table.openKeyLearnPopupMenu(table, e.getX(), e.getY());
		}

		if (column == table.getColumn(
				GUIAutomationTable.COLNAME_IMAGE).getModelIndex()) {
			table.openImagePopupMenu(table, e.getX(), e.getY());
		}
	}
}