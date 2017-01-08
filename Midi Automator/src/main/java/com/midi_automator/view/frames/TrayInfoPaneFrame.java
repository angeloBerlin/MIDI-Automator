package com.midi_automator.view.frames;

import javax.swing.JFrame;

import org.springframework.stereotype.Component;

@Component
public class TrayInfoPaneFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final String MESSAGE_WINDOWS = "The program window will be minimized to the system tray.";
	public static final String MESSAGE_MAC = "The program window will be minimized to the menu bar.";

	private String message;

	public TrayInfoPaneFrame() {

		if (System.getProperty("os.name").contains("Mac")) {
			message = MESSAGE_MAC;
		}

		if (System.getProperty("os.name").contains("Windows")) {
			message = MESSAGE_WINDOWS;
		}
	}

	public String getMessage() {
		return message;
	}
}
