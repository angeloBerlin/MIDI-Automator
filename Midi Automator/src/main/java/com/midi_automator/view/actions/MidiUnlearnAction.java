package com.midi_automator.view.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

/**
 * Deletes the learned midi signature from the component
 * 
 * @author aguelle
 * 
 */
@org.springframework.stereotype.Component
public class MidiUnlearnAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiLearnService midiLearnService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		JMenuItem menuItem = (JMenuItem) e.getSource();

		JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
		Component component = popupMenu.getInvoker();

		// Previous button
		if (component.getName().equals(MainFrame.NAME_PREV_BUTTON)) {
			midiLearnService
					.unsetMidiSignature(MidiLearnService.KEY_MIDI_LEARN_PREVIOUS_BUTTON);
			return;
		}

		// Next button
		if (component.getName().equals(MainFrame.NAME_NEXT_BUTTON)) {
			midiLearnService
					.unsetMidiSignature(MidiLearnService.KEY_MIDI_LEARN_NEXT_BUTTON);
			return;
		}

		// File list
		if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {
			mainFrame.getFileList().setLastSelectedIndex();

			if (component instanceof JList) {
				JList<?> list = (JList<?>) component;
				midiLearnService.unsetMidiSignature(
						MidiLearnService.KEY_MIDI_LEARN_FILE_LIST_ENTRY,
						list.getSelectedIndex());
			}

			return;
		}

		// Automation Table
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {

			if (component instanceof JTable) {
				JTable table = (JTable) component;
				TableModel model = table.getModel();

				int columnIndex = table.getColumn(
						GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE)
						.getModelIndex();
				model.setValueAt(null, table.getSelectedRow(), columnIndex);
			}
		}
	}
}
