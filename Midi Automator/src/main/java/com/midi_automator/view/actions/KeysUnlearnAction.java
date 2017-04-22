package com.midi_automator.view.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

/**
 * Deletes the learned keys from the component
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class KeysUnlearnAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem menuItem = (JMenuItem) e.getSource();

		JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
		Component component = popupMenu.getInvoker();

		// Automation Table
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {

			if (component instanceof JTable) {
				JTable table = (JTable) component;
				TableModel model = table.getModel();

				int columnIndex = table.getColumn(
						GUIAutomationConfigurationTable.COLNAME_KEYS)
						.getModelIndex();
				model.setValueAt(null, table.getSelectedRow(), columnIndex);
			}
		}
	}
}
