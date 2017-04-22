package com.midi_automator.view.menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.midi_automator.view.actions.MidiLearnAction;
import com.midi_automator.view.actions.MidiLearnCancelAction;
import com.midi_automator.view.actions.MidiUnlearnAction;

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

	@Autowired
	private MidiLearnAction midiLearnAction;
	@Autowired
	private MidiLearnCancelAction midiCancelAction;
	@Autowired
	private MidiUnlearnAction midiUnlearnAction;

	/**
	 * Initialize the popup menu
	 */
	public void init() {

		removeAll();
		midiLearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setName(NAME_MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.setEnabled(false);
		midiLearnMenuItem.addActionListener(midiLearnAction);

		midiUnlearnMenuItem = new JMenuItem(MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setName(NAME_MENU_ITEM_MIDI_UNLEARN);
		midiUnlearnMenuItem.setEnabled(false);
		midiUnlearnMenuItem.addActionListener(midiUnlearnAction);

		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
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
}
