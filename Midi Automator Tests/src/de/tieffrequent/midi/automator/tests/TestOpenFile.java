package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestOpenFile extends SikuliTest {

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

}
