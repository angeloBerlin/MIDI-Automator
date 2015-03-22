package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestEditFile extends GUITest {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openPopupMenu("midi_automator.png", null, null);
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
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator_Hello_World.png", TIMEOUT);
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

			if (!GUIAutomations.checkIfFileOpened("Hello_World_Edit_RTF.png")) {
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
			GUIAutomations.cancelDialog();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
