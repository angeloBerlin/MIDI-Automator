package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestDeleteFile extends GUITest {

	@Test
	public void deleteMenuShouldBeDisabledIfListIsEmpty() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open popup menu
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);

			// check for disabled delete menu entry
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "delete_inactive.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			GUIAutomations.focusMidiAutomator();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldEntryShouldBeDeleted() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// delete entry
			GUIAutomations.deleteEntry("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");

			// check if entry was deleted
			GUIAutomations.findMultipleStateRegion(MIN_TIMEOUT,
					"Hello_World_entry.png", "Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");
			fail("Hello World entry still found.");
		} catch (FindFailed | IOException e) {
		}
	}
}
