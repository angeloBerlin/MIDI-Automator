package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestEditFile extends GUITest {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "edit_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void editingFileShouldBeCanceled() {

		try {
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations
					.fillTextField("name_text_field_Hello_World.png", "x");
			GUIAutomations
					.fillTextField("file_text_field_Hello_World.png", "y");
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "file_list_Hello_World.png", TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.fillTextField("name_text_field_Hello_World.png",
					"Hello World Edit");
			GUIAutomations.fillTextField("file_text_field_Hello_World.png",
					currentPath + "/testfiles/Hello World edit.rtf");
			GUIAutomations.saveDialog();

			GUIAutomations.openEntryByDoubleClick("Hello_World_Edit_entry.png",
					"Hello_World_Edit_entry_inactive.png",
					"Hello_World_Edit_entry_active.png");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_Edit_RTF.png",
					"Hello_World_Edit_RTF_inactive.png")) {
				fail("File did not open");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {
		try {
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
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
}
