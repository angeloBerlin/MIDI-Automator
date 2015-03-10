package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestCancelEditFile extends SikuliTest {

	@Test
	public void editingFileShouldBeCanceled() {

		try {

			Automations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			Automations.fillTextField("name_text_field_Hello_World.png", "x");
			Automations.fillTextField("file_text_field_Hello_World.png", "y");
			Automations.cancelDialog();

			// search unmodified midi automator
			Region match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator_Hello_World.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
