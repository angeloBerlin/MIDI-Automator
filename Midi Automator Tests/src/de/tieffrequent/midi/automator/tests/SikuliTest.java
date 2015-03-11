package de.tieffrequent.midi.automator.tests;

import java.io.File;
import java.io.IOException;

import org.sikuli.basics.Settings;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

public class SikuliTest {

	private final float MOVE_MOUSE_DELAY = 0;
	private final boolean CHECK_LAST_SEEN = true;
	protected static final float MAX_SIMILARITY = 0.99f;
	protected static final float DEFAULT_SIMILARITY = 0.97f;
	protected static final int HIGHLIGHT_DURATION = 1;
	protected static final double MIN_TIMEOUT = 0.8;
	protected static final double TIMEOUT = 10;
	protected static final Screen SCREEN = new Screen();
	protected static String screenshotpath;
	protected static String currentPath;
	protected static Region match;
	private static Region searchRegion;

	/**
	 * Constructor
	 */
	public SikuliTest() {

		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.CheckLastSeen = CHECK_LAST_SEEN;
		setMinSimilarity(DEFAULT_SIMILARITY);

		if (System.getProperty("os.name").equals("Mac OS X")) {
			screenshotpath = "screenshots/mac/";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			screenshotpath = "screenshots/windows/";
		}

		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		SikuliTest.setSearchRegion(AllTests.getProgramRegion());
		if (searchRegion == null) {
			SikuliTest.setSearchRegion(SCREEN);
		}
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
		searchRegion.highlight(HIGHLIGHT_DURATION, "green");
		SikuliTest.searchRegion = searchRegion;
	}

	/**
	 * Get the current search region
	 * 
	 * @return The search region
	 */
	public static Region getSearchRegion() {
		return searchRegion;
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
	}
}
