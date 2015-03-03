package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;

public class TestOpenFile extends SikuliTest {

	@Test
	public void newFileShouldBeAdded() {

		Region match = null;

		// open "Hello World"
		try {
			match = SCREEN.wait(screenshotpath + "Hello_World_entry.png",
					TIMEOUT);
		} catch (FindFailed ea) {
			try {
				match = SCREEN.wait(screenshotpath
						+ "Hello_World_entry_inactive.png", TIMEOUT);
			} catch (FindFailed eb) {
				try {
					match = SCREEN.wait(screenshotpath
							+ "Hello_World_entry_active.png", TIMEOUT);
				} catch (FindFailed ec) {
					fail(ec.toString());
				}
			}
		}
		// activate
		try {
			match.click();
			match.doubleClick();
			// strange workaround
			Thread.sleep(1000);
			match.doubleClick();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// check if editor opened
		Settings.MinSimilarity = MIN_SIMILARITY;
		try {
			match = SCREEN.wait(screenshotpath + "Hello_World_RTF_active.png",
					TIMEOUT);
		} catch (FindFailed ea) {
			try {
				match = SCREEN.wait(screenshotpath
						+ "Hello_World_RTF_inactive.png", TIMEOUT);
			} catch (FindFailed eb) {
				fail(eb.toString());
			}
		}
		match.highlight(HIGHLIGHT_DURATION);

		// close editor
		match.click();
		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("q", Key.CMD);
		}
		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type(Key.F4, KeyModifier.WIN | KeyModifier.ALT);
		}
	}
}
