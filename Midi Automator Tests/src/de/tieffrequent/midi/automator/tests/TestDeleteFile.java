package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestDeleteFile extends GUITest {

	@Test
	public void deleteMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openPopupMenu("midi_automator.png", null, null);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "delete_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldEditEntryShouldBeDeleted() {

		try {
			GUIAutomations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			GUIAutomations.deleteEntry("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");

		} catch (FindFailed e) {
			System.out.println("Nothing to delete");
		}

		try {
			GUIAutomations.findMultipleStateRegion(TIMEOUT,
					"Hello_World_entry.png", "Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			fail("Hello World Edit still found.");
		} catch (FindFailed e) {
		}
	}
}
