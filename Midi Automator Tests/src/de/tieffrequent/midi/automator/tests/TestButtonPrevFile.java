package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.MockUpUtils;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestButtonPrevFile extends GUITest {

	@Test
	public void prevButtonNotActiveOnEmptyList() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// check for disabled prev button
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "prev_inactive.png", MAX_TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// cycle second file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// open first file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

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

			// cycle third file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// delete second file
			GUIAutomations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

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

			// cycle second file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// open first file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// add third file
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// cycle third file
			GUIAutomations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations.checkIfFileOpened("Hello_World_3_RTF.png",
					"Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
