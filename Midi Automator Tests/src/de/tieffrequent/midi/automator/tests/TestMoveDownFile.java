package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestMoveDownFile extends SikuliTest {

	@Before
	public void addThreeFiles() {
		try {
			SikuliTest.setSearchRegion(Automations.findMidiAutomatorRegion());
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void moveDownMenuShouldBeDisabledIfListIsEmpty() {

		try {
			Automations.openPopupMenu();
			Region match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			Automations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileShouldBeMovedUp() {
		try {

			Region match;
			Automations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 1.rtf");
			Automations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 2.rtf");
			Automations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 3.rtf");

			Automations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			Automations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "Hello_World_order_123.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// check for inactive menu
			match = Automations.findMultipleStateRegion(
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			match.rightClick();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// close context menu
			Automations.focusMidiAutomator();

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
