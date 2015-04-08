package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestEditFile extends GUITest {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open popup menu
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);

			// check for disabled edit entry
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "edit_inactive.png", MAX_TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void editingFileShouldBeCanceled() {

		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// edit entry
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.fillTextField("name_text_field.png", "x");
			GUIAutomations.fillTextField("file_text_field.png", "y");

			// cancel edit
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "file_list_Hello_World.png", MAX_TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// edit entry
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.fillTextField("name_text_field.png",
					"Hello World Edit");
			GUIAutomations.fillTextField("file_text_field.png", currentPath
					+ "/testfiles/Hello World edit.rtf");
			GUIAutomations.saveDialog();

			// open edited entry
			GUIAutomations.openEntryByDoubleClick("Hello_World_Edit_entry.png",
					"Hello_World_Edit_entry_inactive.png",
					"Hello_World_Edit_entry_active.png");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// check for open edited file
			GUIAutomations.checkIfFileOpened("Hello_World_Edit_RTF.png",
					"Hello_World_Edit_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// edit entry with search dialog
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.openSearchDialog();

			// check search dialog
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "file_chooser.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.cancelDialog();

			GUIAutomations.cancelDialog();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
