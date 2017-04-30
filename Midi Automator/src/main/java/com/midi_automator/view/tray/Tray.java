package com.midi_automator.view.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.Resources;
import com.midi_automator.view.tray.listener.TrayMouseListener;
import com.midi_automator.view.tray.menus.TrayPopupMenu;
import com.midi_automator.view.windows.MainFrame.MainFrame;

@org.springframework.stereotype.Component
public class Tray {

	static Logger log = Logger.getLogger(MainFrame.class.getName());

	@Autowired
	private TrayMouseListener trayMouseListener;
	@Autowired
	private TrayPopupMenu trayPopupMenu;

	private TrayIcon trayIcon;
	private Image image;

	@Autowired
	private Resources resources;

	public static final String NAME = "MIDI Automator";

	/**
	 * Initializes the tray
	 */
	public void init() {

		if (SystemTray.isSupported()) {

			SystemTray tray = SystemTray.getSystemTray();

			if (tray.getTrayIcons().length == 0) {

				String iconFileName = resources.getImagePath() + File.separator
						+ "MidiAutomatorIcon16.png";
				image = Toolkit.getDefaultToolkit().getImage(iconFileName);
				trayPopupMenu.init();
				trayIcon = new TrayIcon(image, NAME);
				trayIcon.addMouseListener(trayMouseListener);

				try {
					tray.add(trayIcon);
				} catch (AWTException e) {
					log.error("Error on adding tray icon.", e);
				}
			}
		}
	}

	/**
	 * Puts the tray to midi learn mode
	 * 
	 */
	public void midiLearnOn() {
		trayPopupMenu.midiLearnOn();
	}

	/**
	 * Puts the tray to normal mode
	 * 
	 */
	public void midiLearnOff() {
		trayPopupMenu.midiLearnOff();
	}
}
