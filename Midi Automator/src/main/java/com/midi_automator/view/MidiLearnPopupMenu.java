package com.midi_automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.view.frames.MainFrame;

public class MidiLearnPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MIDI_UNLEARN = "Midi unlearn";
	private final String MENU_ITEM_MIDI_LEARN = "Midi learn";
	private final String MENU_ITEM_MIDI_LEARN_CANCEL = "Cancel midi learn";

	private final String NAME_MENU_ITEM_MIDI_LEARN = "midi learn";
	private final String NAME_MENU_ITEM_MIDI_UNLEARN = "midi unlearn";

	protected JMenuItem midiLearnMenuItem;
	protected JMenuItem midiUnlearnMenuItem;

	private Action midiLearnAction;
	private Action midiCancelAction;

	protected final MidiAutomator APPLICATION;
	protected MainFrame program_frame;

	/**
	 * Constructor
	 * 
	 * @param parentFrame
	 *            The parent main frame
	 * @param application
	 *            The main application
	 */
	public MidiLearnPopupMenu(JFrame parentFrame, MidiAutomator application) {
		super();

		this.APPLICATION = application;
		if (parentFrame instanceof MainFrame) {
			program_frame = (MainFrame) parentFrame;
		}
		midiLearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setName(NAME_MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setEnabled(false);
		midiLearnAction = new MidiLearnAction(program_frame);
		midiLearnMenuItem.addActionListener(midiLearnAction);

		midiUnlearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setName(NAME_MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setEnabled(false);
		midiUnlearnMenuItem.addActionListener(new MidiUnlearnAction(
				program_frame));

		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);

		midiCancelAction = new MidiLearnCancelAction(program_frame);
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
	class MidiLearnAction extends PopUpMenuAction {

		public MidiLearnAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JMenuItem menuItem = (JMenuItem) e.getSource();
			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			JComponent component = (JComponent) popupMenu.getInvoker();
			APPLICATION.setMidiLearnMode(true, component);
		}
	}

	/**
	 * Deletes the learned midi signature from the component
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiUnlearnAction extends PopUpMenuAction {

		public MidiUnlearnAction(MainFrame PROGRAM_FRAME) {
			super(PROGRAM_FRAME);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JMenuItem menuItem = (JMenuItem) e.getSource();

			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			Component component = popupMenu.getInvoker();
			APPLICATION.unsetMidiSignature(component);

			if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {
				PROGRAM_FRAME.getFileList().setLastSelectedIndex();
			}
		}
	}

	/**
	 * Cancels the midi learn mode and put the application to the normal state.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiLearnCancelAction extends PopUpMenuAction {

		public MidiLearnCancelAction(MainFrame parentFrame) {
			super(parentFrame);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			APPLICATION.setMidiLearnMode(false, null);
		}
	}
}