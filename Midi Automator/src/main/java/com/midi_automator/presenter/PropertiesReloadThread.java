package com.midi_automator.presenter;

/**
 * This is a helper thread for loading the properties. As java.awt.Robot methods
 * are not allowed to be run from the EventDispatcherThread, this thread will
 * work as wrapping thread.
 * 
 * @author aguelle
 * 
 */
public class PropertiesReloadThread extends Thread {

	private final MidiAutomator APPLICATION;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The MIDI Automator application
	 */
	public PropertiesReloadThread(MidiAutomator application) {
		this.APPLICATION = application;
	}

	@Override
	public void run() {
		APPLICATION.reloadProperties();
	}

}
