package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class MoveUpDownFileITCase extends IntegrationTestCase {

	@Test
	public void moveUpDownMenuShouldBeDisabledIfListIsEmpty() {

		try {
			SikuliXAutomations.openMidiAutomator();

			// check for disabled menu entry
			SikuliXAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);
			SikuliXAutomations.checkResult("move_up_inactive.png");

			SikuliXAutomations.openPopupMenu("midi_automator.png", null, null,
					LOW_SIMILARITY);
			SikuliXAutomations.checkResult("move_down_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			// move down entry
			SikuliXAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			SikuliXAutomations.moveDownEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			SikuliXAutomations.checkResult("Hello_World_order_123.png");

			// check for inactive menu
			Region match = SikuliXAutomations.findMultipleStateRegion(MAX_TIMEOUT,
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			match.rightClick();
			SikuliXAutomations.checkResult("move_down_inactive.png");

			// check if moving was saved
			SikuliXAutomations.closeMidiAutomator();
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.checkResult("Hello_World_order_123.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			// move entries
			SikuliXAutomations.moveUpEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");
			SikuliXAutomations.moveUpEntry("Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			// check for correct order
			SikuliXAutomations.checkResult("Hello_World_order_312.png");

			// check for inactive menu
			Region match = SikuliXAutomations.findMultipleStateRegion(MAX_TIMEOUT,
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_active.png",
					"Hello_World_3_entry_inactive.png");

			match.rightClick();
			SikuliXAutomations.checkResult("move_up_inactive.png");

			// check if moving was saved
			SikuliXAutomations.closeMidiAutomator();
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.checkResult("Hello_World_order_312.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
