package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

public class TestCancelAddFile extends SikuliTest {

	@Test
	public void addingFileShouldBeCanceled() {

		try {
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);

			// open add dialog
			match.rightClick();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "add.png", TIMEOUT);
			match.click();

			// type name
			Settings.MinSimilarity = MIN_SIMILARITY;
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "name_text_field.png", TIMEOUT);
			match.click();
			SCREEN.paste("x");

			// type file
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "file_text_field.png", TIMEOUT);
			match.click();
			SCREEN.paste("y");

			// cancel
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "cancel_button.png", TIMEOUT);
			match.click();

			// search unmodified midi automator
			Settings.MinSimilarity = MAX_SIMILARITY;
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
