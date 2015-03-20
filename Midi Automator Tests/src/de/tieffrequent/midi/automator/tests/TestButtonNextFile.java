package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestButtonNextFile extends GUITest {

	@Test
	public void nextButtonNotActiveOnEmptyList() {
		try {
			SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "next_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextFileShouldBeOpenedInCycle() {
		try {
			GUIAutomations.restartMidiAutomator();
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// open second file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			GUIAutomations.restartMidiAutomator();
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

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

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			GUIAutomations.restartMidiAutomator();
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// open first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// open second file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

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

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// cycle first file
			GUIAutomations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
