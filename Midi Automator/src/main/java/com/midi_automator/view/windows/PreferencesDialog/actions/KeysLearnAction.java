package com.midi_automator.view.windows.PreferencesDialog.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.KeyLearnService;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Puts the application to the key learn mode for the selected component.
 * 
 * @author aguelle
 * 
 */
@Component
public class KeysLearnAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected KeyLearnService keyLearnService;
	@Autowired
	protected MainFrame mainFrame;

	@Override
	public void actionPerformed(ActionEvent e) {

		JMenuItem menuItem = (JMenuItem) e.getSource();
		JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
		JComponent component = (JComponent) popupMenu.getInvoker();

		mainFrame.keyLearnOn(component);

		if (component.getName().equals(GUIAutomationTable.NAME)) {

			if (component instanceof JTable) {
				JTable automationsTable = (JTable) component;
				keyLearnService.activateKeyLearn(
						KeyLearnService.KEY_KEY_LEARN_AUTOMATION,
						automationsTable.getSelectedRow());

			}
			return;
		}
	}
}
