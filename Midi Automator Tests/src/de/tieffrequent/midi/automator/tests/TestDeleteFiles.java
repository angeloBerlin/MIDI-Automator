package de.tieffrequent.midi.automator.tests;

import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestDeleteFiles extends SikuliTest {

	@Test
	public void allHelloWorldEntriesShouldBeDeleted() {

		try {
			while (true) {
				Automations.deleteEntry("Hello_World_entry_snippet.png",
						"Hello_World_entry_snippet_active.png",
						"Hello_World_entry_snippet_inactive.png");
			}
		} catch (FindFailed e1) {
			System.out.println("Nothing to delete");
		}
	}
}
