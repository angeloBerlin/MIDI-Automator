package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestAddFile extends SikuliTest {

	@Test
	public void newFileShouldBeAdded() {
		try {
			Region match;

			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

			// search new entry
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "Hello_World_entry.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileChooserOfAddDialogShouldBeOpened() {
		try {
			Automations.openAddDialog();
			Automations.openSearchDialog();
			Automations.cancelDialog();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
