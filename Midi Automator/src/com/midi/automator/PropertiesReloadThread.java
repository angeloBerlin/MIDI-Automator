package com.midi.automator;

/**
 * This is a helper thread for loading the properties. As java.awt.Robot methods
 * are not allowed to be run from the EventDispatcherThread, this thread will
 * work as wrapping thread.
 * 
 * @author aguelle
 * 
 */
public class PropertiesReloadThread extends Thread {

	private IApplication application;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The MIDI automator alpplication
	 */
	public PropertiesReloadThread(IApplication application) {
		this.application = application;
	}

	@Override
	public void run() {
		application.reloadProperties();
	}

}
