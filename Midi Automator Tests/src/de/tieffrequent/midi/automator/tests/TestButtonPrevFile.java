package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestButtonPrevFile extends SikuliTest {

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
	public void prevButtonNotActiveOnEmptyList() {
		try {
			SikuliTest.getSearchRegion().wait(
					screenshotpath + "prev_inactive.png", TIMEOUT);
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {
		try {
			Automations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			Automations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// cycle second file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// open first file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
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

			// cycle third file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
			}

			// delete second file
			Automations.deleteEntry("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// open first file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
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

			// cycle second file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				fail("'Hello World 2.rtf' did not open");
			}

			// open first file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				fail("'Hello World 1.rtf' did not open");
			}

			// add third file
			Automations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf");

			// cycle third file
			Automations.prevFile();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!Automations
					.checkIfFileOpened("Hello_World_3_RTF_inactive.png")) {
				fail("'Hello World 3.rtf' did not open");
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
