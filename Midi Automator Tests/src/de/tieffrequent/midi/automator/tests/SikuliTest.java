package de.tieffrequent.midi.automator.tests;

import org.sikuli.basics.Settings;
import org.sikuli.script.Screen;

public class SikuliTest {

	private final float MOVE_MOUSE_DELAY = 0;
	private final float MIN_SIMILARITY = 0.97f;
	protected final int HIGHLIGHT_DURATION = 1;
	protected final double TIMEOUT = 5;
	protected final Screen SCREEN;
	protected String screenshotpath;

	public SikuliTest() {

		Settings.MoveMouseDelay = MOVE_MOUSE_DELAY;
		Settings.MinSimilarity = MIN_SIMILARITY;
		SCREEN = new Screen();

		System.out.println(System.getProperty("os.name"));

		if (System.getProperty("os.name").equals("Mac OS X")) {
			screenshotpath = "screenshots/mac/";
		}
	}
}
