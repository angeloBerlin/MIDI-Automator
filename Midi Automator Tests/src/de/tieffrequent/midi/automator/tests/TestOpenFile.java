package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;

public class TestOpenFile extends GUITest {

	@Test
	public void helloWorldFileShouldBeOpened() {

		try {
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			GUIAutomations.openEntryByDoubleClick(
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");
		} catch (FindFailed e) {
			fail(e.toString());
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!GUIAutomations.checkIfFileOpened("Hello_World_RTF.png",
				"Hello_World_RTF_inactive.png")) {
			fail("File did not open");
		}
	}

	@Test
	public void helloWorldÄÖÜFileShouldBeOpened() {

		try {
			GUIAutomations.addFile("Hello World ÄÖÜ", currentPath
					+ "/testfiles/Hello World ÄÖÜ.rtf");
			GUIAutomations.openEntryByDoubleClick(
					"Hello_World_ÄÖÜ_entry_active.png",
					"Hello_World_ÄÖÜ_entry_inactive.png",
					"Hello_World_ÄÖÜ_entry.png");
		} catch (FindFailed e) {
			fail(e.toString());
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!GUIAutomations.checkIfFileOpened("Hello_World_ÄÖÜ_RTF.png",
				"Hello_World_ÄÖÜ_RTF_inactive.png")) {
			fail("File did not open");
		}
	}
}
