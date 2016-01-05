package com.midi_automator.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.presenter.services.MidiService;
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
	protected MidiAutomator presenter;
	@Autowired
	protected MainFrame mainFrame;

	@Autowired
	protected MidiService midiService;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

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
			midiService.setMidiLearnMode(true, component);
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
			midiService.unsetMidiSignature(component);

			if (component.getName().equals(MainFrame.NAME_FILE_LIST)) {
				mainFrame.getFileList().setLastSelectedIndex();
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
			midiService.setMidiLearnMode(false, null);
		}
	}
}
