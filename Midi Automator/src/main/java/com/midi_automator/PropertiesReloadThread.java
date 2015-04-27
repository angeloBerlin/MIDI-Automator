package com.midi_automator;

/**
 * This is a helper thread for loading the properties. As java.awt.Robot methods
 * are not allowed to be run from the EventDispatcherThread, this thread will
 * work as wrapping thread.
 * 
 * @author aguelle
 * 
 */
public class PropertiesReloadThread extends Thread {

	private MidiAutomator application;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The MIDI Automator application
	 */
	public PropertiesReloadThread(MidiAutomator application) {
		this.application = application;
	}

	@Override
	public void run() {
		application.reloadProperties();
	}

}
