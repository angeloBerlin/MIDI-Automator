package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class OpenItemFileITCase extends IntegrationTestCase {

	@Test
	public void helloWorldFileShouldBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openEntryByDoubleClick("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");

			// check if file opened
			SikuliXAutomations.checkIfFileOpened("Hello_World_RTF.png",
					"Hello_World_RTF_inactive.png");

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
	public void helloWorldÄÖÜFileShouldBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_ÄÖÜ.mido");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openEntryByDoubleClick("Hello_World_ÄÖÜ_entry.png",
					"Hello_World_ÄÖÜ_entry_active.png",
					"Hello_World_ÄÖÜ_entry_inactive.png");

			// check if file opened
			SikuliXAutomations.checkIfFileOpened("Hello_World_ÄÖÜ_RTF.png",
					"Hello_World_ÄÖÜ_RTF_inactive.png");

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
	public void emptyFileInfoShallBeShown() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openEntryByDoubleClick("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png");

			// check for failure
			SikuliXAutomations.checkResult("error_file_not_found");

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
