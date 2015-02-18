package de.tieffrequent.midi.automator.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tieffrequent.midi.automator.IApplication;

public class MidiLearnPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MIDI_LEARN = "Midi learn";
	private final String MENU_ITEM_MIDI_LEARN_CANCEL = "Cancel midi learn";

	protected JMenuItem midiLearnMenuItem;
	private Action midiLearnAction;
	private Action midiCancelAction;

	protected IApplication application;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The main application
	 */
	public MidiLearnPopupMenu(IApplication application) {
		super();

		this.application = application;

		midiLearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setEnabled(true);
		midiLearnAction = new MidiLearnAction();
		midiLearnMenuItem.addActionListener(midiLearnAction);
		add(midiLearnMenuItem);

		midiCancelAction = new MidiLearnCancelAction();
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

	/**
	 * Puts the application to the midi learn mode for the selected component.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiLearnAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {

			JMenuItem menuItem = (JMenuItem) ae.getSource();
			JPopupMenu popupMenu = (JPopupMenu) menuItem.getParent();
			JComponent component = (JComponent) popupMenu.getInvoker();
			application.setMidiLearnMode(true, component);
		}
	}

	/**
	 * Cancels the midi learn mode and put the application to the normal state.
	 * 
	 * @author aguelle
	 * 
	 */
	class MidiLearnCancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
			application.setMidiLearnMode(false, null);
		}
	}
}
