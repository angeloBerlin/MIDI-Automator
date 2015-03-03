package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.test.helper.SikuliHelper;

public class TestCancelAddFile extends SikuliTest {

	@Test
	public void addingFileShouldBeCanceled() {

		Region searchRegion;
		Region match;

		try {

			searchRegion = SCREEN.wait("screenshots/midi_automator.png",
					TIMEOUT);
			match = searchRegion;

			// open add dialog
			match.rightClick();
			match = searchRegion.wait("screenshots/add.png", TIMEOUT);
			match.click();

			// type name
			match = searchRegion.wait("screenshots/name_text_field.png",
					TIMEOUT);
			match.click();
			SCREEN.type(SikuliHelper.translateUSToKeyboard("x"));

			// type file
			match = searchRegion.wait("screenshots/file_text_field.png",
					TIMEOUT);
			match.click();
			SCREEN.type(SikuliHelper.translateUSToKeyboard("y"));

			// cancel
			match = searchRegion.wait("screenshots/cancel_button.png", TIMEOUT);
			match.click();

			// search unmodified midi automator
			match = searchRegion
					.wait("screenshots/midi_automator.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
