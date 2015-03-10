package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestOpenFile extends SikuliTest {

	@Before
	public void addHelloWorldFile() {
		try {
			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());

			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldFileShouldBeOpened() {

		try {
			Automations.openEntryByDoubleClick("Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
		} catch (FindFailed e) {
			fail(e.toString());
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!Automations.checkIfFileOpened("Hello_World_RTF_inactive.png")) {
			fail("File did not open");
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
