package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class SwitchButtonsITCase extends IntegrationTestCase {

	@Test
	public void nextButtonNotActiveOnEmptyList() {
		try {
			SikuliXAutomations.openMidiAutomator();

			// check for inactive next button
			SikuliXAutomations.checkResult("next_inactive.png");

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
	public void nextFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// open first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// cycle first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

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
	public void nextThirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			SikuliXAutomations.openMidiAutomator();

			// open first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// delete second file
			SikuliXAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

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
	public void nextThirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// open first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// ad third file
			SikuliXAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open third file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			SikuliXAutomations.nextFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

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
	public void prevButtonNotActiveOnEmptyList() {
		try {
			SikuliXAutomations.openMidiAutomator();

			// check for disabled prev button
			SikuliXAutomations.checkResult("prev_inactive.png");

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
	public void prevFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// cycle second file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open first file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

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
	public void prevThirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			SikuliXAutomations.openMidiAutomator();

			// cycle third file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// delete second file
			SikuliXAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

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
	public void prevThirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// cycle second file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open first file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// add third file
			SikuliXAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// cycle third file
			SikuliXAutomations.prevFile();
			SikuliXAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

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
