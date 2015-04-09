package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;

public class TestEditFile extends GUITest {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openMidiAutomator();
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);

			// check for disabled edit entry
			GUIAutomations.checkResult("edit_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void editingFileShouldBeCanceled() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.openMidiAutomator();

			// edit entry
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.fillTextField("name_text_field.png", "x");
			GUIAutomations.fillTextField("file_text_field.png", "y");
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			GUIAutomations.checkResult("file_list_Hello_World.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.openMidiAutomator();

			// edit entry
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.fillTextField("name_text_field.png",
					"Hello World Edit");
			GUIAutomations.fillTextField("file_text_field.png", currentPath
					+ "/testfiles/Hello World edit.rtf");
			GUIAutomations.saveDialog();

			// check for open edited file
			GUIAutomations.openEntryByDoubleClick("Hello_World_Edit_entry.png",
					"Hello_World_Edit_entry_inactive.png",
					"Hello_World_Edit_entry_active.png");
			GUIAutomations.checkIfFileOpened("Hello_World_Edit_RTF.png",
					"Hello_World_Edit_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.openMidiAutomator();

			// edit entry with search dialog
			GUIAutomations.openEditDialog("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
			GUIAutomations.openSearchDialog();

			// check search dialog
			GUIAutomations.checkResult("file_chooser.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.cancelDialog();
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
