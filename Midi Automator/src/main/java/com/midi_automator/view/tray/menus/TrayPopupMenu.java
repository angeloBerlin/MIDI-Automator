package com.midi_automator.view.tray.menus;

import java.awt.Component;

import javax.swing.JMenuItem;

import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.presenter.services.PresenterService;
import com.midi_automator.view.windows.MainFrame.actions.HideShowMainFrameAction;
import com.midi_automator.view.windows.menus.MidiLearnPopupMenu;

@org.springframework.stereotype.Component
public class TrayPopupMenu extends MidiLearnPopupMenu {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tray popup menu";
	public static final String MENU_ITEM_OPEN_MIDI_AUTOMATOR = "Open...";
	public static final String MENU_ITEM_HIDE_MIDI_AUTOMATOR = "Hide...";

	private JMenuItem hideShowMenuItem;

	@Autowired
	private HideShowMainFrameAction hideShowAction;

	@Autowired
	private MidiService midiService;
	@Autowired
	private PresenterService presenterService;

	/**
	 * Initializes the popup menu
	 */
	public void init() {
		super.init();
		setName(NAME);
		hideShowMenuItem = new JMenuItem(MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		hideShowMenuItem.addActionListener(hideShowAction);

		removeAll();
		add(hideShowMenuItem);
		add(midiLearnMenuItem);
		add(midiUnlearnMenuItem);
	}

	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x - invoker.getLocation().x,
				y - invoker.getLocation().y);
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
					.setText(TrayPopupMenu.MENU_ITEM_OPEN_MIDI_AUTOMATOR);
		} else {
			hideShowMenuItem
					.setText(TrayPopupMenu.MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		}
	}

	/**
	 * Opens the popup menu
	 * 
	 * @param x
	 *            The x location
	 * @param y
	 *            The y location
	 */
	public void open(int x, int y) {
		setLocation(x, y);
		setVisible(true);
	}

	/**
	 * Closes the popup menu
	 */
	public void close() {
		setVisible(false);
	}
}
