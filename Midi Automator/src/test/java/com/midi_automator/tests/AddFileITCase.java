package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class AddFileITCase extends IntegrationTestCase {

	@Test
	public void newFileShouldBeAdded() {
		try {
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

			// search new entry
			SikuliXAutomations.checkResult("Hello_World_entry.png");

			// check if new entry was saved
			SikuliXAutomations.closeMidiAutomator();
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.checkResult("Hello_World_entry.png");

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
	public void fileChooserOfAddDialogShouldBeOpened() {
		try {
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.openAddDialog();
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
	public void addingFileShouldBeCanceled() {

		try {
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.fillTextField("name_text_field.png", "x");
			SikuliXAutomations.fillTextField("file_text_field.png", "y");
			SikuliXAutomations.cancelDialog();

			// search unmodified midi automator
			SikuliXAutomations.checkResult("midi_automator.png");

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
	public void addingEmptyFileNameShouldNotBePossible() {

		try {
			SikuliXAutomations.openMidiAutomator();

			// adde empty file
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.saveDialog();

			// search unmodified midi automator
			SikuliXAutomations.checkResult("midi_automator.png");

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
	public void addingMoreFilesThan128ShouldBeImpossible() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
			SikuliXAutomations.openMidiAutomator();

			// add file 129
			SikuliXAutomations.addFile("Hello World 129", currentPath
					+ "/testfiles/Hello World.rtf");

			// check for failure
			SikuliXAutomations.checkResult("error_129th_file_added.png");

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
}
