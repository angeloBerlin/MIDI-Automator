package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestAddFile extends SikuliTest {

	private String currentPath = "";

	public TestAddFile() {
		super();

		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void newFileShouldBeAdded() {

		Region searchRegion;
		Region match;

		try {

			searchRegion = SCREEN.wait(screenshotpath + "midi_automator.png",
					TIMEOUT);
			match = searchRegion;

			// open add dialog
			match.rightClick();
			match = searchRegion.wait(screenshotpath + "add.png", TIMEOUT);
			match.click();

			// type name
			Settings.MinSimilarity = MIN_SIMILARITY;
			match = searchRegion.wait(screenshotpath + "name_text_field.png",
					TIMEOUT);
			Settings.MinSimilarity = MAX_SIMILARITY;
			match.click();
			SCREEN.paste("Hello World");

			// type file
			match = searchRegion.wait(screenshotpath + "file_text_field.png",
					TIMEOUT);
			match.click();
			SCREEN.paste(currentPath + "/testfiles/Hello World.rtf");

			// save
			match = searchRegion.wait(screenshotpath + "save_button.png",
					TIMEOUT);
			match.click();

			// search new entry
			match = searchRegion.wait(screenshotpath + "Hello_World_entry.png",
					TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
