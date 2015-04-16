package com.midi.automator.tests.utils;

import java.io.File;
import java.io.IOException;

import org.sikuli.basics.Settings;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.midi.automator.tests.AllTests;

public class SikuliAutomation {

	private final float MOVE_MOUSE_DELAY = 0;
	private final boolean CHECK_LAST_SEEN = true;
	protected static final float MAX_SIMILARITY = 0.999f;
	protected static final float HIGH_SIMILARITY = 0.99f;
	protected static final float DEFAULT_SIMILARITY = 0.97f;
	protected static final float LOW_SIMILARITY = 0.7f;
	protected static final int HIGHLIGHT_DURATION = 1;
	private static final int HIGHLIGHT_SEARCH_REGION_DURATION = 1;
	private static boolean HIGHLIGHT_SEARCH_REGION = false;
	protected static final double MIN_TIMEOUT = 0.5;
	protected static final double DEFAULT_TIMEOUT = 5;
	protected static final double MAX_TIMEOUT = 20;
	protected static final Screen SCREEN = new Screen();
	protected static String screenshotpath;
	protected static String currentPath;
	private static Region searchRegion;

	/**
	 * Constructor
	 */
	public SikuliAutomation() {

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

		SikuliAutomation.setSearchRegion(AllTests.getProgramRegion());
		if (searchRegion == null) {
			SikuliAutomation.setSearchRegion(SCREEN);
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

		System.out.println("Set search region to: "
				+ searchRegion.toStringShort());
		if (HIGHLIGHT_SEARCH_REGION) {
			searchRegion.highlight(HIGHLIGHT_SEARCH_REGION_DURATION, "green");
		}

		SikuliAutomation.searchRegion = searchRegion;
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
		System.out.println("Set MinSimilarity to: " + minSimilarity);
	}
}
