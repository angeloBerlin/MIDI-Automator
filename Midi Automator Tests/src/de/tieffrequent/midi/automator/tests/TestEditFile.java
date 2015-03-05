package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;

public class TestEditFile extends SikuliTest {

	@Test
	public void helloWorldShouldBeEdited() {

		Region match = null;

		try {
			// open edit dialog
			try {
				match = SikuliTest.getSearchRegion().wait(
						screenshotpath + "Hello_World_entry.png", TIMEOUT);
				match.click();
			} catch (FindFailed ea) {
				try {
					match = SikuliTest.getSearchRegion().wait(
							screenshotpath + "Hello_World_entry_inactive.png",
							TIMEOUT);
				} catch (FindFailed eb) {
					try {
						match = SikuliTest.getSearchRegion()
								.wait(screenshotpath
										+ "Hello_World_entry_active.png",
										TIMEOUT);
					} catch (FindFailed ec) {
						fail(ec.toString());
					}
				}
			}
			match.click();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			match.rightClick();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "edit.png", TIMEOUT);
			match.click();

			// type name
			match = SikuliTest.getSearchRegion()
					.wait(screenshotpath + "name_text_field_Hello_World.png",
							TIMEOUT);
			Settings.MinSimilarity = MAX_SIMILARITY;
			SCREEN.paste("Hello World Edit");

			// use search dialog
			match = SCREEN.wait(screenshotpath + "search_button.png", TIMEOUT);
			match.click();
			match = SCREEN.wait(screenshotpath + "file_chooser.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			if (System.getProperty("os.name").equals("Mac OS X")) {
				match = SCREEN.wait(screenshotpath + "cancel_button.png",
						TIMEOUT);
				match.click();
			}

			if (System.getProperty("os.name").equals("Windows 7")) {
				match = SCREEN.wait(screenshotpath + "abbrechen_button.png",
						TIMEOUT);
				match.click();
			}

			// type file
			match = SikuliTest.getSearchRegion()
					.wait(screenshotpath + "file_text_field_Hello_World.png",
							TIMEOUT);
			match.click();
			if (System.getProperty("os.name").equals("Mac OS X")) {
				SCREEN.type("a", KeyModifier.CMD);
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				SCREEN.type("a", KeyModifier.CTRL);
			}

			SCREEN.paste(currentPath + "/testfiles/Hello World edit.rtf");

			// save
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "save_button.png", TIMEOUT);
			match.click();

			// search new entry
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "Hello_World_Edit_entry.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}

		// open edited file
		TestOpenFile
				.openHelloWorldEdit("Hello_World_Edit_entry.png",
						"Hello_World_Edit_entry_inactive.png",
						"Hello_World_Edit_entry_active.png",
						"Hello_World_Edit_RTF.png");

	}

}
