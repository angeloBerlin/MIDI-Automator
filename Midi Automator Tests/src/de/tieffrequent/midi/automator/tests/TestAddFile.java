package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestAddFile extends SikuliTest {

	@Test
	public void newFileShouldBeAdded() {

		Region match;

		try {
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);

			// open add dialog
			match.rightClick();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "add.png", TIMEOUT);
			match.click();

			// type name
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "name_text_field.png", TIMEOUT);
			match.click();
			SCREEN.paste("Hello World");

			// use search dialog
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "search_button.png", TIMEOUT);
			match.click();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "file_chooser.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			if (System.getProperty("os.name").equals("Mac OS X")) {
				match = SikuliTest.getSearchRegion().wait(
						screenshotpath + "cancel_button.png", TIMEOUT);
				match.click();
			}

			if (System.getProperty("os.name").equals("Windows 7")) {
				match = SikuliTest.getSearchRegion().wait(
						screenshotpath + "abbrechen_button.png", TIMEOUT);
				match.click();
			}

			// type file
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "file_text_field.png", TIMEOUT);
			match.click();
			SCREEN.paste(currentPath + "/testfiles/Hello World.rtf");

			// save
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "save_button.png", TIMEOUT);
			match.click();

			// search new entry
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "Hello_World_entry.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
