package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestEditFile extends SikuliTest {

	@Before
	public void addHelloWorldFile() {
		try {
			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			Automations.openPopupMenu();
			Region match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "edit_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			Automations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void editingFileShouldBeCanceled() {

		try {
			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
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

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			Automations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			Automations.fillTextField("name_text_field_Hello_World.png",
					"Hello World Edit");
			Automations.fillTextField("file_text_field_Hello_World.png",
					currentPath + "/testfiles/Hello World edit.rtf");
			Automations.saveDialog();

			Automations.openEntryByDoubleClick("Hello_World_Edit_entry.png",
					"Hello_World_Edit_entry_inactive.png",
					"Hello_World_Edit_entry_active.png");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations.checkIfFileOpened("Hello_World_Edit_RTF.png")) {
				fail("File did not open");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {
		try {
			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			Automations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			Automations.openSearchDialog();
			Automations.cancelDialog();

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
