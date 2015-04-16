package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;

public class TestMoveUpDownFile extends GUITest {

	@Test
	public void moveUpDownMenuShouldBeDisabledIfListIsEmpty() {

		try {
			GUIAutomations.openMidiAutomator();

			// check for disabled menu entry
			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);
			GUIAutomations.checkResult("move_up_inactive.png");

			GUIAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);
			GUIAutomations.checkResult("move_down_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void fileShouldBeMovedDown() {
		try {

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_world_312.mido");
			GUIAutomations.openMidiAutomator();

			// move down entry
			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			GUIAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			GUIAutomations.checkResult("Hello_World_order_123.png");

			// check for inactive menu
			Region match = GUIAutomations.findMultipleStateRegion(MAX_TIMEOUT,
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			match.rightClick();
			GUIAutomations.checkResult("move_down_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void fileShouldBeMovedUp() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			GUIAutomations.openMidiAutomator();

			// move entries
			GUIAutomations.moveUpEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			GUIAutomations.moveUpEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			GUIAutomations.checkResult("Hello_World_order_312.png");

			// check for inactive menu
			Region match = GUIAutomations.findMultipleStateRegion(MAX_TIMEOUT,
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			match.rightClick();
			GUIAutomations.checkResult("move_up_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
