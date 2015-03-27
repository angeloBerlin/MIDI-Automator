package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestAddFile extends GUITest {

	@Test
	public void newFileShouldBeAdded() {
		try {
			Region match;

			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

			// search new entry
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "Hello_World_entry.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileChooserOfAddDialogShouldBeOpened() {
		try {
			GUIAutomations.openAddDialog();
			GUIAutomations.openSearchDialog();
			// check search dialog
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "file_chooser.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.cancelDialog();
			GUIAutomations.cancelDialog();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void addingFileShouldBeCanceled() {

		try {

			GUIAutomations.openAddDialog();
			GUIAutomations.fillTextField("name_text_field.png", "x");
			GUIAutomations.fillTextField("file_text_field.png", "y");
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void addingEmptyFileNameShouldNotBePossible() {

		try {

			GUIAutomations.openAddDialog();
			GUIAutomations.saveDialog();

			// search unmodified midi automator
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	// @Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

		try {

			for (int i = 1; i <= 129; i++) {
				GUIAutomations.addFile("Hello World " + i, currentPath
						+ "/testfiles/Hello World.rtf");
			}
		} catch (FindFailed e) {
			e.printStackTrace();
		}

		try {
			GUIAutomations.findMultipleStateRegion(MIN_TIMEOUT,
					"Hello_World_129_entry.png",
					"Hello_World_129_entry_active.png",
					"Hello_World_129_entry_inactive.png");
			fail("More than 128 entries added!");
		} catch (FindFailed e) {
		}
	}
}
