package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class AddFileITCase extends IntegrationTestCase {

	@Test
	public void newFileShouldBeAdded() {
		try {
			GUIAutomations.openMidiAutomator();
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

			// search new entry
			GUIAutomations.checkResult("Hello_World_entry.png");

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
	public void fileChooserOfAddDialogShouldBeOpened() {
		try {
			GUIAutomations.openMidiAutomator();
			GUIAutomations.openAddDialog();
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

	@Test
	public void addingFileShouldBeCanceled() {

		try {
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openAddDialog();
			GUIAutomations.fillTextField("name_text_field.png", "x");
			GUIAutomations.fillTextField("file_text_field.png", "y");
			GUIAutomations.cancelDialog();

			// search unmodified midi automator
			GUIAutomations.checkResult("midi_automator.png");

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
	public void addingEmptyFileNameShouldNotBePossible() {

		try {
			GUIAutomations.openMidiAutomator();

			// adde empty file
			GUIAutomations.openAddDialog();
			GUIAutomations.saveDialog();

			// search unmodified midi automator
			GUIAutomations.checkResult("midi_automator.png");

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
	public void addingMoreFilesThan128ShouldBeImpossible() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
			GUIAutomations.openMidiAutomator();

			// add file 129
			GUIAutomations.addFile("Hello World 129", currentPath
					+ "/testfiles/Hello World.rtf");

			// check for failure
			GUIAutomations.checkResult("error_129th_file_added.png");

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
}
