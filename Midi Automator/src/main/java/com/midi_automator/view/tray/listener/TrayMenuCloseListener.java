package com.midi_automator.view.tray.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.view.tray.menus.TrayPopupMenu;

/**
 * Closes the tray popup menu on mouse click.
 * 
 * @author aguelle
 *
 */
@Component
public class TrayMenuCloseListener extends MouseAdapter {

	@Autowired
	private TrayPopupMenu trayPopupMenu;

	@Override
	public void mouseClicked(MouseEvent e) {
		trayPopupMenu.close();
	}
}
