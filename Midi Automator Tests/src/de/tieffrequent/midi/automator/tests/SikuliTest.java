package de.tieffrequent.midi.automator.tests;

import org.sikuli.basics.Settings;
import org.sikuli.script.Screen;

public class SikuliTest {

	private final float MOVE_MOUSE_DELAY = 0;
	private final boolean CHECK_LAST_SEEN = false;
	protected final float MAX_SIMILARITY = 0.99f;
	protected final float MIN_SIMILARITY = 0.97f;
	protected final int HIGHLIGHT_DURATION = 1;
	protected final double TIMEOUT = 7;
	protected final Screen SCREEN;
	protected String screenshotpath;

	public SikuliTest() {

		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.MinSimilarity = MAX_SIMILARITY;
		Settings.CheckLastSeen = CHECK_LAST_SEEN;
		SCREEN = new Screen();

		if (System.getProperty("os.name").equals("Mac OS X")) {
			screenshotpath = "screenshots/mac/";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			screenshotpath = "screenshots/windows/";
		}
	}
}
