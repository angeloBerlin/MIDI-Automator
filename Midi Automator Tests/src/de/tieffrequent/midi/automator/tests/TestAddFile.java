package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.MockUpUtils;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestAddFile extends GUITest {

	@Test
	public void newFileShouldBeAdded() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// add file
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

			// search new entry
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "Hello_World_entry.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
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
					screenshotpath + "file_chooser.png", MAX_TIMEOUT);
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

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			GUIAutomations.openAddDialog();
			GUIAutomations.fillTextField("name_text_field.png", "x");
			GUIAutomations.fillTextField("file_text_field.png", "y");
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void addingEmptyFileNameShouldNotBePossible() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// adde empty file
			GUIAutomations.openAddDialog();
			GUIAutomations.saveDialog();

			// search unmodified midi automator
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// add file 129
			GUIAutomations.addFile("Hello World 129", currentPath
					+ "/testfiles/Hello World.rtf");

			// check for failure
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "error_129th_file_added.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
