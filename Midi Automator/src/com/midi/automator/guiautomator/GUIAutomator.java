package com.midi.automator.guiautomator;

import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

import com.midi.automator.IDeActivateable;
import com.midi.automator.utils.SystemUtils;

/**
 * 
 * @author aguelle
 * 
 *         This class automates GUI interactions.
 */
public class GUIAutomator extends Thread implements IDeActivateable {

	private final float MOVE_MOUSE_DELAY = 0;
	private final float MIN_SIMILARITY = 0.99f;
	private final boolean CHECK_LAST_SEEN = true;

	private final Screen SCREEN;
	private volatile boolean running = true;
	private final boolean DEBUG;
	private boolean active = true;

	private GUIAutomation[] guiAutomations;

	/**
	 * Standard Constructor
	 * 
	 * @param debug
	 *            Indicates if the program is working in debug mode
	 */
	public GUIAutomator(boolean debug) {
		this.DEBUG = debug;
		SCREEN = new Screen();
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
	 * @param debug
	 *            Indicates if the program is working in debug mode
	 */
	public GUIAutomator(GUIAutomation[] guiAutomations, boolean debug) {
		this(debug);
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

		if (DEBUG) {
			for (GUIAutomation guiAutomation : guiAutomations) {
				System.out.println(this.getClass().getCanonicalName() + "("
						+ getName() + "): Activated Automation: "
						+ guiAutomation.toString());
			}
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

			if (DEBUG) {
				System.out.println(this.getClass().getCanonicalName()
						+ "("
						+ getName()
						+ "): Search for match of \""
						+ SystemUtils.replaceSystemVariables(guiAutomation
								.getImagePath()) + "\"");
			}

			startingTime = System.currentTimeMillis();

			try {
				Thread.sleep(guiAutomation.getMinDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Match match = SCREEN.wait(SystemUtils
					.replaceSystemVariables(guiAutomation.getImagePath()),
					guiAutomation.getTimeout());
			found = true;

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

			if (DEBUG) {
				System.out.println(this.getClass().getCanonicalName()
						+ "("
						+ getName()
						+ "): Found match on screen for \""
						+ SystemUtils.replaceSystemVariables(guiAutomation
								.getImagePath()) + "\" (Timeout after "
						+ (System.currentTimeMillis() - startingTime) + " ms)");
			}

		} catch (FindFailed e) {

			if (DEBUG) {
				System.out.println(this.getClass().getCanonicalName()
						+ "("
						+ getName()
						+ "): Could not find match on screen for \""
						+ SystemUtils.replaceSystemVariables(guiAutomation
								.getImagePath()) + "\" (Timeout after "
						+ (System.currentTimeMillis() - startingTime) + " ms)");
			}
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
