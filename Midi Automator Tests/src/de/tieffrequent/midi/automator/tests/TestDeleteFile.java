package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestDeleteFile extends SikuliTest {

	@Before
	public void addHelloWorldFile() {
		try {
			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void deleteMenuShouldBeDisabledIfListIsEmpty() {

		try {
			Automations.openPopupMenu();
			Region match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "delete_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			Automations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldEditEntryShouldBeDeleted() {

		try {
			Automations.addFile("Hello World", currentPath
					+ "/testfiles/Hello World.rtf");
			Automations.deleteEntry("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");

		} catch (FindFailed e) {
			System.out.println("Nothing to delete");
		}

		try {
			Automations.findMultipleStateRegion("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			fail("Hello World Edit still found.");
		} catch (FindFailed e) {
		}
	}
}
