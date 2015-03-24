package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.sikuli.script.FindFailed;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class GUITest extends SikuliAutomation {

	@Before
	public void setSearchRegionToMidiAutomator() {
		try {
			GUIAutomations.focusMidiAutomator();
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@After
	public void deleteAllHelloWorldEntries() {

		try {
			while (true) {
				GUIAutomations.deleteEntry("Hello_World_entry_snippet.png",
						"Hello_World_entry_snippet_active.png",
						"Hello_World_entry_snippet_inactive.png");
			}
		} catch (FindFailed e) {
		}
	}
}
