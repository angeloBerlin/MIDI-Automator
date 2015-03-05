package de.tieffrequent.midi.automator.tests;

import java.io.File;
import java.io.IOException;

import org.sikuli.basics.Settings;
import org.sikuli.script.Screen;

public class SikuliTest {

	private final float MOVE_MOUSE_DELAY = 0;
	private final boolean CHECK_LAST_SEEN = false;
	protected static final float MAX_SIMILARITY = 0.99f;
	protected static final float MIN_SIMILARITY = 0.97f;
	protected static final int HIGHLIGHT_DURATION = 1;
	protected static final double TIMEOUT = 7;
	protected static final Screen SCREEN = new Screen();
	protected static String screenshotpath;
	protected static String currentPath;

	public SikuliTest() {

		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.MinSimilarity = MAX_SIMILARITY;
		Settings.CheckLastSeen = CHECK_LAST_SEEN;

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
	}
}
