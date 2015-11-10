package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class EditFileITCase extends IntegrationTestCase {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		try {
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);

			// check for disabled edit entry
			SikuliXAutomations.checkResult("edit_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			// edit entry
			SikuliXAutomations.openEditDialog("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			SikuliXAutomations.fillTextField("name_text_field.png", "x");
			SikuliXAutomations.fillTextField("file_text_field.png", "y");
			SikuliXAutomations.cancelDialog();

			// search unmodified midi automator
			SikuliXAutomations.checkResult("file_list_Hello_World.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			// edit entry
			SikuliXAutomations.openEditDialog("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			SikuliXAutomations.fillTextField("name_text_field.png",
					"Hello World Edit");
			SikuliXAutomations.fillTextField("file_text_field.png", currentPath
					+ "/testfiles/Hello World edit.rtf");
			SikuliXAutomations.saveDialog();

			// check for open edited file
			SikuliXAutomations.openEntryByDoubleClick("Hello_World_Edit_entry.png",
					"Hello_World_Edit_entry_inactive.png",
					"Hello_World_Edit_entry_active.png");
			SikuliXAutomations.checkIfFileOpened("Hello_World_Edit_RTF.png",
					"Hello_World_Edit_RTF_inactive.png");

			// check if edited entry was saved
			SikuliXAutomations.closeMidiAutomator();
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.checkResult("Hello_World_Edit_entry.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			// edit entry with search dialog
			SikuliXAutomations.openEditDialog("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			SikuliXAutomations.openSearchDialog();

			// check search dialog
			SikuliXAutomations.checkResult("file_chooser.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void sendingMidiSignatureShouldBeShown() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// edit entry 1
			SikuliXAutomations.openEditDialog("Hello_World_1_entry.png",
					"Hello_World_entry_1_active.png",
					"Hello_World_entry_1_inactive.png");

			// check midi signature
			SikuliXAutomations.checkResult("midi_signature_sending_1.png",
					SikuliXAutomations.DEFAULT_SIMILARITY);

			SikuliXAutomations.cancelDialog();

			// edit entry 2
			SikuliXAutomations.openEditDialog("Hello_World_2_entry.png",
					"Hello_World_entry_2_active.png",
					"Hello_World_entry_2_inactive.png");

			// check midi signature
			SikuliXAutomations.checkResult("midi_signature_sending_2.png",
					SikuliXAutomations.DEFAULT_SIMILARITY);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
