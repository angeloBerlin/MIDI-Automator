package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestEditFile extends SikuliTest {

	@Test
	public void helloWorldShouldBeEdited() {

		Region match = null;

		try {

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
			Automations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			Automations.openSearchDialog();
			Automations.cancelDialog();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
