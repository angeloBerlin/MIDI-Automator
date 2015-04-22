package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;

public class TestOpenFile extends GUITest {

	@Test
	public void helloWorldFileShouldBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openEntryByDoubleClick("Hello_World_entry.png",
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png");

			// check if file opened
			GUIAutomations.checkIfFileOpened("Hello_World_RTF.png",
					"Hello_World_RTF_inactive.png");

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
	public void helloWorldÄÖÜFileShouldBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_ÄÖÜ.mido");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openEntryByDoubleClick("Hello_World_ÄÖÜ_entry.png",
					"Hello_World_ÄÖÜ_entry_active.png",
					"Hello_World_ÄÖÜ_entry_inactive.png");

			// check if file opened
			GUIAutomations.checkIfFileOpened("Hello_World_ÄÖÜ_RTF.png",
					"Hello_World_ÄÖÜ_RTF_inactive.png");

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
