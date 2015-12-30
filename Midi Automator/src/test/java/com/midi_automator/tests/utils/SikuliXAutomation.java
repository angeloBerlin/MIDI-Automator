package com.midi_automator.tests.utils;

import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class SikuliXAutomation {

	private final float MOVE_MOUSE_DELAY = 0.2f;
	private final boolean CHECK_LAST_SEEN = true;
	protected final static float HIGH_SIMILARITY = 0.99f;
	protected final static float DEFAULT_SIMILARITY = 0.95f;
	protected static final float LOW_SIMILARITY = 0.6f;
	protected final static int HIGHLIGHT_DURATION = 1;
	protected final static double DEFAULT_TIMEOUT = 8;
	protected final static double MAX_TIMEOUT = 20;
	protected static Screen SCREEN;
	protected static String screenshotpath;
	private static Region searchRegion;

	/**
	 * 
	 * @param screen
	 *            The Sikuli screen
	 */
	public SikuliXAutomation(Screen screen) {

		SikuliXAutomation.SCREEN = screen;
		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.CheckLastSeen = CHECK_LAST_SEEN;
		setMinSimilarity(DEFAULT_SIMILARITY);

		if (System.getProperty("os.name").equals("Mac OS X")) {
			screenshotpath = "screenshots/mac/";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			screenshotpath = "screenshots/windows/";
		}

		setSearchRegion(screen);
	}

	/**
	 * Sets the search region for all tests
	 * 
	 * @param searchRegion
	 *            the region to search in
	 */
	public static void setSearchRegion(Region searchRegion) {

		if (searchRegion == null) {
			return;
		}

		System.out.println("Set search region to: "
				+ searchRegion.toStringShort());

		SikuliXAutomation.searchRegion = searchRegion;
	}

	/**
	 * Sets the minimum similarity
	 * 
	 * @param minSimilarity
	 *            the minimum similarity
	 */
	protected static void setMinSimilarity(float minSimilarity) {
		Settings.MinSimilarity = minSimilarity;
		Settings.CheckLastSeenSimilar = minSimilarity;
		System.out.println("Set MinSimilarity to: " + minSimilarity);
	}

	/**
	 * Finds a region that can have multiple states, i.e. active, inactive,
	 * unfocused
	 * 
	 * @param timeout
	 *            the timeout to search for every state
	 * @param states
	 *            the different states of the region
	 * @return The found region
	 * @throws FindFailed
	 */
	public static Region findMultipleStateRegion(double timeout,
			String... states) throws FindFailed {

		Region match;
		FindFailed findFailed = null;

		for (String state : states) {
			if (state != null) {
				try {
					match = searchRegion.wait(screenshotpath + state, timeout);
					return match;
				} catch (FindFailed e) {
					findFailed = e;
					System.out.println(state
							+ " not found. Trying next state...");
				}
			}
		}

		throw findFailed;
	}

	/**
	 * Closes the current focused program.
	 */
	public static void closeFocusedProgram() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("q", Key.CMD);
		}
		if (System.getProperty("os.name").contains("Windows")) {
			SCREEN.type(Key.F4, KeyModifier.WIN | KeyModifier.ALT);
		}
	}
}
