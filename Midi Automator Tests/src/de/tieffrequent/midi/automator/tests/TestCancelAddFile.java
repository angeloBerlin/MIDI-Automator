package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestCancelAddFile extends SikuliTest {

	@Test
	public void addingFileShouldBeCanceled() {

		try {

			Automations.openAddDialog();
			Automations.fillTextField("name_text_field.png", "x");
			Automations.fillTextField("file_text_field.png", "y");
			Automations.cancelDialog();

			// search unmodified midi automator
			setMinSimilarity(MAX_SIMILARITY);
			Region match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
