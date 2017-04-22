package com.midi_automator.view.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.Resources;
import com.midi_automator.view.actions.HideShowMainFrameAction;
import com.midi_automator.view.frames.MainFrame;

@org.springframework.stereotype.Component
public class Tray {

	static Logger log = Logger.getLogger(MainFrame.class.getName());

	private PopupMenu trayPopupMenu;
	private MenuItem hideShowMenuItem;
	private MenuItem midiLearnMenuItem;
	private MenuItem midiUnLearnMenuItem;
	private TrayIcon trayIcon;
	private Image image;

	@Autowired
	private MainFrame programFrame;
	@Autowired
	private Resources resources;
	@Autowired
	private HideShowMainFrameAction hideShowAction;

	public static final String NAME_TRAY = "MIDI Automator";
	public static final String MENU_ITEM_OPEN_MIDI_AUTOMATOR = "Open...";
	public static final String MENU_ITEM_HIDE_MIDI_AUTOMATOR = "Hide...";
	public static final String MENU_ITEM_MIDI_LEARN_OPEN_HIDE = "Midi learn";
	public static final String MENU_ITEM_MIDI_UNLEARN_OPEN_HIDE = "Midi unlearn";

	/**
	 * Initializes the tray
	 */
	public void init() {

		if (SystemTray.isSupported()) {

			SystemTray tray = SystemTray.getSystemTray();

			if (tray.getTrayIcons().length == 0) {

				trayPopupMenu = createTrayPopupMenu();

				String iconFileName = resources.getImagePath() + File.separator
						+ "MidiAutomatorIcon16.png";
				image = Toolkit.getDefaultToolkit().getImage(iconFileName);
				trayIcon = new TrayIcon(image, NAME_TRAY, trayPopupMenu);

				try {
					tray.add(trayIcon);
				} catch (AWTException e) {
					log.error("Error on adding tray icon.", e);
				}
			}
		}
	}

	/**
	 * Creates the tray popup menu
	 * 
	 * @return The popup menu
	 */
	private PopupMenu createTrayPopupMenu() {

		PopupMenu popupMenu = new PopupMenu();
		hideShowMenuItem = new MenuItem(MENU_ITEM_HIDE_MIDI_AUTOMATOR);
		midiLearnMenuItem = new MenuItem(MENU_ITEM_MIDI_LEARN_OPEN_HIDE);
		midiUnLearnMenuItem = new MenuItem(MENU_ITEM_MIDI_UNLEARN_OPEN_HIDE);

		hideShowMenuItem.addActionListener(hideShowAction);

		popupMenu.add(hideShowMenuItem);
		popupMenu.add(midiLearnMenuItem);
		popupMenu.add(midiUnLearnMenuItem);

		return popupMenu;
	}

	public MenuItem getHideShowMenuItem() {
		return hideShowMenuItem;
	}

}
