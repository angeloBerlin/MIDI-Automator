package com.midi_automator.view.tray.menus;

import java.awt.MenuItem;
import java.awt.PopupMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.actions.MidiLearnAction;
import com.midi_automator.view.actions.MidiLearnCancelAction;
import com.midi_automator.view.actions.MidiUnlearnAction;
import com.midi_automator.view.windows.MainFrame.actions.HideShowMainFrameAction;

@Component
public class TrayPopupMenu extends PopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tray popup menu";
	public static final String MENU_ITEM_OPEN_MIDI_AUTOMATOR = "Open...";
	public static final String MENU_ITEM_HIDE_MIDI_AUTOMATOR = "Hide...";
	public static final String MENU_ITEM_MIDI_LEARN = "Midi learn";
	public static final String MENU_ITEM_MIDI_LEARN_CANCEL = "Cancel midi learn";
	public static final String MENU_ITEM_MIDI_UNLEARN = "Midi unlearn";

	private MenuItem hideShowMenuItem;
	private MenuItem midiLearnMenuItem;
	private MenuItem midiUnLearnMenuItem;

	@Autowired
	private HideShowMainFrameAction hideShowAction;
	@Autowired
	private MidiLearnAction midiLearnAction;
	@Autowired
	private MidiLearnCancelAction midiLearnCancelAction;
	@Autowired
	private MidiUnlearnAction midiUnlearnAction;

	/**
	 * Initializes the popup menu
	 */
	public void init() {
		setName(NAME);

		hideShowMenuItem = new MenuItem(MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		midiLearnMenuItem = new MenuItem(MENU_ITEM_MIDI_LEARN);
		midiUnLearnMenuItem = new MenuItem(MENU_ITEM_MIDI_UNLEARN);

		hideShowMenuItem.addActionListener(hideShowAction);
		midiLearnMenuItem.addActionListener(midiLearnAction);
		midiUnLearnMenuItem.addActionListener(midiUnlearnAction);

		add(hideShowMenuItem);
		add(midiLearnMenuItem);
		add(midiUnLearnMenuItem);
	}

	/**
	 * Sets the state of the menu according to the visibility of the main frame.
	 * 
	 * @param state
	 *            <TRUE> mainFrame is hidden, <FALSE> main frame is shown
	 */
	public void setHiddenState(boolean state) {

		if (state) {
			hideShowMenuItem
					.setLabel(TrayPopupMenu.MENU_ITEM_OPEN_MIDI_AUTOMATOR);
		} else {
			hideShowMenuItem
					.setLabel(TrayPopupMenu.MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		}
	}

	/**
	 * Puts the frame to midi learn mode
	 */
	public void midiLearnOn() {

		midiLearnMenuItem.setLabel(MENU_ITEM_MIDI_LEARN_CANCEL);
		midiLearnMenuItem.removeActionListener(midiLearnAction);
		midiLearnMenuItem.addActionListener(midiLearnCancelAction);
	}

	/**
	 * Puts the frame to normal mode.
	 */
	public void midiLearnOff() {

		midiLearnMenuItem.setLabel(MENU_ITEM_MIDI_LEARN);
		midiLearnMenuItem.removeActionListener(midiLearnCancelAction);
		midiLearnMenuItem.addActionListener(midiLearnAction);
	}
}
