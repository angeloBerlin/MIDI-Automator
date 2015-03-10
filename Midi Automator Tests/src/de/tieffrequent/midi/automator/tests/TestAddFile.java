package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestAddFile extends SikuliTest {

	@Before
	public void addHelloWorldFile() {
		try {
			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

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

	@After
	public void deleteAllFiles() {

		try {
			while (true) {
				Automations.deleteEntry("Hello_World_entry_snippet.png",
						"Hello_World_entry_snippet_active.png",
						"Hello_World_entry_snippet_inactive.png");
			}
		} catch (FindFailed e) {
		}
	}
}
