package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestButtonNextFile extends GUITest {

	@Test
	public void nextButtonNotActiveOnEmptyList() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// check for inactive next button
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "next_inactive.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// delete second file
			GUIAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// ad third file
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open third file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png");

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
