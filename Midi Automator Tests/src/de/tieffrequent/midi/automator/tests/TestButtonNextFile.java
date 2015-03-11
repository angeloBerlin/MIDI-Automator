package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestButtonNextFile extends SikuliTest {

	@Before
	public void addThreeFiles() {
		try {
			Automations.openExitMenu();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Automations.openMidiAutomator();

			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextButtonNotActiveOnEmptyList() {
		try {
			SikuliTest.setMinSimilarity(MAX_SIMILARITY);
			SikuliTest.getSearchRegion().wait(
					screenshotpath + "next_inactive.png", TIMEOUT);
			SikuliTest.setMinSimilarity(DEFAULT_SIMILARITY);
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void nextFileShouldBeOpenedInCycle() {
		try {
			Automations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			Automations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// open first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// open second file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// cycle first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterDeletingSecondFile() {
		try {
			Automations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			Automations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");
			Automations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// delete second file
			Automations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open third file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// cycle first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void thirdFileShouldBeOpenedAfterAddingOnIndex2() {
		try {
			Automations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			Automations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// open first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// open second file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// ad third file
			Automations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// open third file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// cycle first file
			Automations.nextFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open in cycle");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@After
	public void deleteAllFiles() {

		try {
			while (true) {
				Automations.deleteEntry("Hello_World_entry_snippet.png",
						"Hello_World_entry_snippet_active.png",
						"Hello_World_entry_snippet_inactive.png");
			}
		} catch (FindFailed e) {
		}
	}
}
