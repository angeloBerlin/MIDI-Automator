package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestMoveDownFile extends GUITest {

	@Test
	public void moveDownMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openPopupMenu("midi_automator.png", null, null);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileShouldBeMovedUp() {
		try {

			Region match;
			GUIAutomations.addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 2.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 3.rtf");

			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "Hello_World_order_123.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// check for inactive menu
			match = GUIAutomations.findMultipleStateRegion(
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			match.rightClick();
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// close context menu
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
