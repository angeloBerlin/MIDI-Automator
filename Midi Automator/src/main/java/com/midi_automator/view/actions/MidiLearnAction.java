package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

/**
 * Puts the application to the midi learn mode for the selected component.
 * 
 * @author aguelle
 * 
 */
@Component
public class MidiLearnAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MidiLearnService midiLearnService;

	public MidiLearnAction() {
		super();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		JMenuItem menuItem = (JMenuItem) e.getSource();
		JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
		JComponent component = (JComponent) popupMenu.getInvoker();

		mainFrame.midiLearnOn(component);

		// Previous button
		if (component.getName().equals(MainFrame.NAME_PREV_BUTTON)) {
			midiLearnService
					.activateMidiLearn(MidiLearnService.KEY_MIDI_LEARN_PREVIOUS_BUTTON);
			return;
		}

		// Next button
		if (component.getName().equals(MainFrame.NAME_NEXT_BUTTON)) {
			midiLearnService
					.activateMidiLearn(MidiLearnService.KEY_MIDI_LEARN_NEXT_BUTTON);
			return;
		}

		// File list
		if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {
			if (component instanceof JList) {
				JList<?> list = (JList<?>) component;
				midiLearnService.activateMidiLearn(
						MidiLearnService.KEY_MIDI_LEARN_FILE_LIST_ENTRY,
						list.getSelectedIndex());
			}
			return;
		}

		// GUI automation trigger
		if (component.getName().equals(GUIAutomationConfigurationTable.NAME)) {

			if (component instanceof JTable) {
				JTable automationsTable = (JTable) component;
				midiLearnService.activateMidiLearn(
						MidiLearnService.KEY_MIDI_LEARN_AUTOMATION_TRIGGER,
						automationsTable.getSelectedRow());

			}
			return;
		}
	}
}
