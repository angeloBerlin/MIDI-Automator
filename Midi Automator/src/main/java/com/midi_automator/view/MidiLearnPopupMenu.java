package com.midi_automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.presenter.Presenter;
import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.MainFrame;

@Controller
public class MidiLearnPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String MENU_ITEM_MIDI_UNLEARN = "Midi unlearn";
	public static final String MENU_ITEM_MIDI_LEARN = "Midi learn";
	public static final String MENU_ITEM_MIDI_LEARN_CANCEL = "Cancel midi learn";

	public static final String NAME_MENU_ITEM_MIDI_LEARN = "midi learn";
	public static final String NAME_MENU_ITEM_MIDI_UNLEARN = "midi unlearn";

	protected JMenuItem midiLearnMenuItem;
	protected JMenuItem midiUnlearnMenuItem;

	private Action midiLearnAction;
	private Action midiCancelAction;

	@Autowired
	protected Presenter presenter;
	@Autowired
	protected MainFrame mainFrame;

	@Autowired
	private MidiLearnService midiLearnService;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		midiLearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setName(NAME_MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setEnabled(false);
		midiLearnAction = new MidiLearnAction(mainFrame);
		midiLearnMenuItem.addActionListener(midiLearnAction);

		midiUnlearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setName(NAME_MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setEnabled(false);
		midiUnlearnMenuItem.addActionListener(new MidiUnlearnAction(mainFrame));

		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);

		midiCancelAction = new MidiLearnCancelAction(mainFrame);
	}

	/**
	 * Puts the frame to midi learn mode
	 */
	public void midiLearnOn() {

		midiLearnMenuItem.setText(MENU_ITEM_MIDI_LEARN_CANCEL);
		midiLearnMenuItem.removeActionListener(midiLearnAction);
		midiLearnMenuItem.addActionListener(midiCancelAction);
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		midiLearnMenuItem.setText(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.removeActionListener(midiCancelAction);
		midiLearnMenuItem.addActionListener(midiLearnAction);
	}

	public JMenuItem getMidiLearnMenuItem() {
		return midiLearnMenuItem;
	}

	public JMenuItem getMidiUnlearnMenuItem() {
		return midiUnlearnMenuItem;
	}

	/**
	 * Puts the application to the midi learn mode for the selected component.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiLearnAction extends AbstractPopUpMenuAction {

		public MidiLearnAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

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
			if (component.getName()
					.equals(GUIAutomationConfigurationTable.NAME)) {

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

	/**
	 * Deletes the learned midi signature from the component
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiUnlearnAction extends AbstractPopUpMenuAction {

		public MidiUnlearnAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

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
			if (component.getName()
					.equals(GUIAutomationConfigurationTable.NAME)) {

				if (component instanceof JTable) {
					JTable table = (JTable) component;
					TableModel model = table.getModel();

					int columnIndex = table
							.getColumn(
									GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE)
							.getModelIndex();
					model.setValueAt(null, table.getSelectedRow(), columnIndex);
				}
			}
		}
	}

	/**
	 * Cancels the midi learn mode and put the application to the normal state.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiLearnCancelAction extends AbstractPopUpMenuAction {

		public MidiLearnCancelAction(MainFrame mainFrame) {
			super(mainFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			midiLearnService.setMidiLearnMode(false);
		}
	}
}
