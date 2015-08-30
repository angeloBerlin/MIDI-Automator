package com.midi_automator.guiautomator;

import org.apache.log4j.Logger;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import com.midi_automator.presenter.IDeActivateable;
import com.midi_automator.utils.SystemUtils;

/**
 * 
 * @author aguelle
 * 
 *         This class automates GUI interactions.
 */
public class GUIAutomator extends Thread implements IDeActivateable {

	static Logger log = Logger.getLogger(GUIAutomator.class.getName());

	private final float MOVE_MOUSE_DELAY = 0;
	private final float MIN_SIMILARITY = 0.99f;
	private final boolean CHECK_LAST_SEEN = true;

	private final Screen SCREEN;
	private volatile boolean running = true;
	private boolean active = true;

	private GUIAutomation[] guiAutomations;

	/**
	 * Standard Constructor
	 * 
	 * @param debug
	 *            Indicates if the program is working in debug mode
	 */
	public GUIAutomator() {
		SCREEN = new Screen(0);
		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.MinSimilarity = MIN_SIMILARITY;
		Settings.CheckLastSeenSimilar = MIN_SIMILARITY;
		Settings.CheckLastSeen = CHECK_LAST_SEEN;
	}

	/**
	 * Constructor
	 * 
	 * @param guiAutomations
	 *            An array of GUI automations to run
	 */
	public GUIAutomator(GUIAutomation[] guiAutomations) {
		this();
		setGUIAutomations(guiAutomations);
	}

	@Override
	public void run() {
		while (running) {

			if (isActive()) {
				try {
					for (GUIAutomation guiAutomation : guiAutomations) {
						triggerAutomation(guiAutomation);
					}
				} catch (IndexOutOfBoundsException e) {
				}
			}
		}
	}

	/**
	 * Let's the thread terminate
	 */
	public void terminate() {
		running = false;
	}

	/**
	 * Sets the GUI automations that shall be run.
	 * 
	 * @param clickimagepaths
	 *            Paths to the images
	 */
	public void setGUIAutomations(GUIAutomation[] guiAutomations) {
		this.guiAutomations = guiAutomations;

		for (GUIAutomation guiAutomation : guiAutomations) {
			log.info(("(" + getName() + "): Activated Automation: " + guiAutomation
					.toString()));
		}

	}

	/**
	 * Runs a configured GUI automation.
	 * 
	 * @param clickimagepath
	 *            path to the image that should be searched
	 */
	private void triggerAutomation(GUIAutomation guiAutomation) {

		// always
		if (guiAutomation.getTrigger()
				.equals(GUIAutomation.CLICKTRIGGER_ALWAYS)) {
			runAutomation(guiAutomation);
		}

		// once
		if (guiAutomation.getTrigger().equals(GUIAutomation.CLICKTRIGGER_ONCE)) {
			if (guiAutomation.isActive()) {
				if (runAutomation(guiAutomation)) {
					guiAutomation.setActive(false);
				}
			}
		}

		// on change
		if (guiAutomation.getTrigger().equals(
				GUIAutomation.CLICKTRIGGER_ONCE_PER_CHANGE)) {
			if (guiAutomation.isActive()) {
				if (runAutomation(guiAutomation)) {
					guiAutomation.setActive(false);
				}
			}
		}

		// on midi
		if (guiAutomation.getTrigger().equals(GUIAutomation.CLICKTRIGGER_MIDI)) {

			if (guiAutomation.isActive()) {
				if (runAutomation(guiAutomation)) {
					guiAutomation.setActive(false);
				}
			}
		}
	}

	/**
	 * Activates all automations that shall be run only once per change.
	 */
	public void activateOncePerChangeAutomations() {

		for (GUIAutomation guiAutomation : guiAutomations) {
			if (guiAutomation.getTrigger().equals(
					GUIAutomation.CLICKTRIGGER_ONCE_PER_CHANGE)) {
				log.info("Activate automation once per change:" + guiAutomation);
				guiAutomation.setActive(true);
			}
		}
	}

	/**
	 * Activates all automations that shall be run only once per change.
	 * 
	 * @param midiSignature
	 *            The midi signture that shall invoke the automation
	 */
	public void activateMidiAutomations(String midiSignature) {

		for (GUIAutomation guiAutomation : guiAutomations) {
			if ((guiAutomation.getTrigger().equals(
					GUIAutomation.CLICKTRIGGER_MIDI) && (guiAutomation
					.getMidiSignature().equals(midiSignature)))) {
				guiAutomation.setActive(true);
			}
		}
	}

	/**
	 * Searches for the region and does the desired action.
	 * 
	 * @param guiAutomation
	 *            The GUI automation
	 * 
	 * @return <TRUE> if the screenshot was found, <FALSE> if not found or
	 *         automation not active
	 */
	private boolean runAutomation(GUIAutomation guiAutomation) {

		boolean found = false;
		double startingTime = 0.0;

		try {

			log.debug("("
					+ getName()
					+ "): Search for match of \""
					+ SystemUtils.replaceSystemVariables(guiAutomation
							.getImagePath()) + "\"");

			startingTime = System.currentTimeMillis();

			Match match = SCREEN.wait(SystemUtils
					.replaceSystemVariables(guiAutomation.getImagePath()),
					guiAutomation.getTimeout());
			found = true;

			try {
				Thread.sleep(guiAutomation.getMinDelay());
			} catch (InterruptedException e) {
				log.error("Delay before GUI automation run failed.", e);
			}

			// left click
			if (guiAutomation.getType().equals(GUIAutomation.CLICKTYPE_LEFT)) {
				match.click();
			}

			// right click
			if (guiAutomation.getType().equals(GUIAutomation.CLICKTYPE_RIGHT)) {
				match.rightClick();
			}

			// double click
			if (guiAutomation.getType().equals(GUIAutomation.CLICKTYPE_DOUBLE)) {
				match.doubleClick();
			}

			log.info("("
					+ getName()
					+ "): Found match on screen for \""
					+ SystemUtils.replaceSystemVariables(guiAutomation
							.getImagePath()) + "\" (Timeout after "
					+ (System.currentTimeMillis() - startingTime) + " ms)");

		} catch (FindFailed e) {

			log.info("("
					+ getName()
					+ "): Could not find match on screen for \""
					+ SystemUtils.replaceSystemVariables(guiAutomation
							.getImagePath()) + "\" (Timeout after "
					+ (System.currentTimeMillis() - startingTime) + " ms)");
		}

		return found;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
}
