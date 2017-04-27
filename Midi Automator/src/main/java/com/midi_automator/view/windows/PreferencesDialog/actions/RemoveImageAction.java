package com.midi_automator.view.windows.PreferencesDialog.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import com.midi_automator.view.windows.MainFrame.actions.AbstractMainFramePopUpMenuAction;
import com.midi_automator.view.windows.PreferencesDialog.ScaleableImageIcon;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Removes the screenshot image
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class RemoveImageAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		JMenuItem menuItem = (JMenuItem) e.getSource();
		JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
		Component component = popupMenu.getInvoker();

		// Automation Table
		if (component.getName().equals(GUIAutomationTable.NAME)) {

			if (component instanceof JTable) {
				JTable table = (JTable) component;
				TableModel model = table.getModel();

				int columnIndex = table.getColumn(
						GUIAutomationTable.COLNAME_IMAGE)
						.getModelIndex();
				model.setValueAt(new ScaleableImageIcon(),
						table.getSelectedRow(), columnIndex);
			}
		}
	}
}
