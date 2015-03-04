package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestCancelAddFile extends SikuliTest {

	@Test
	public void addingFileShouldBeCanceled() {

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
			match.click();
			SCREEN.paste("x");

			// type file
			match = searchRegion.wait(screenshotpath + "file_text_field.png",
					TIMEOUT);
			match.click();
			SCREEN.paste("y");

			// cancel
			match = searchRegion.wait(screenshotpath + "cancel_button.png",
					TIMEOUT);
			match.click();

			// search unmodified midi automator
			Settings.MinSimilarity = MAX_SIMILARITY;
			match = searchRegion.wait(screenshotpath + "midi_automator.png",
					TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
