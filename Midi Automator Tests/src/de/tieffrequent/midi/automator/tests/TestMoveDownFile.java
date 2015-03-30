package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.MockUpUtils;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class TestMoveDownFile extends GUITest {

	@Test
	public void moveDownMenuShouldBeDisabledIfListIsEmpty() {

		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open poupmenu
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);

			// check for disabled menu entry
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void fileShouldBeMovedUp() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_world_312.mido");
			GUIAutomations.restartMidiAutomator();

			// move down entry
			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "Hello_World_order_123.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// check for inactive menu
			match = GUIAutomations.findMultipleStateRegion(TIMEOUT,
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			match.rightClick();
			SikuliAutomation.setMinSimilarity(MAX_SIMILARITY);
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "move_down_inactive.png", TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			// close context menu
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
