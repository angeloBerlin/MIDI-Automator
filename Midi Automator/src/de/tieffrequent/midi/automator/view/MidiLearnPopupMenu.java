package de.tieffrequent.midi.automator.view;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.tieffrequent.midi.automator.IApplication;
import de.tieffrequent.midi.automator.view.frames.MainFrame;

public class MidiLearnPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private final String MENU_ITEM_MIDI_LEARN = "Midi learn";
	private final String MENU_ITEM_MIDI_LEARN_CANCEL = "Cancel midi learn";

	protected JMenuItem midiLearnMenuItem;
	private Action midiLearnAction;
	private Action midiCancelAction;

	protected IApplication application;
	protected MainFrame mainFrame;

	/**
	 * Constructor
	 * 
	 * @param parentFrame
	 *            The parent main frame
	 * @param application
	 *            The main application
	 */
	public MidiLearnPopupMenu(JFrame parentFrame, IApplication application) {
		super();

		this.application = application;
		if (parentFrame instanceof MainFrame) {
			mainFrame = (MainFrame) parentFrame;
		}
		midiLearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setEnabled(true);
		midiLearnAction = new MidiLearnAction(mainFrame);
		midiLearnMenuItem.addActionListener(midiLearnAction);
		add(midiLearnMenuItem);

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
			application.setMidiLearnMode(true, component);
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
			application.setMidiLearnMode(false, null);
		}
	}
}
