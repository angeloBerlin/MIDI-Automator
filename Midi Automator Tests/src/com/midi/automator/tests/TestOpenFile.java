package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;

public class TestOpenFile extends GUITest {

	@Test
	public void helloWorldFileShouldBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
			GUIAutomations.restartMidiAutomator();

			// open file
			GUIAutomations.openEntryByDoubleClick(
					"Hello_World_entry_active.png",
					"Hello_World_entry_inactive.png", "Hello_World_entry.png");

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// check if file opened
			GUIAutomations.checkIfFileOpened("Hello_World_RTF.png",
					"Hello_World_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void helloWorldÄÖÜFileShouldBeOpened() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_ÄÖÜ.mido");
			GUIAutomations.restartMidiAutomator();
			GUIAutomations.openEntryByDoubleClick(
					"Hello_World_ÄÖÜ_entry_active.png",
					"Hello_World_ÄÖÜ_entry_inactive.png",
					"Hello_World_ÄÖÜ_entry.png");

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			GUIAutomations.checkIfFileOpened("Hello_World_ÄÖÜ_RTF.png",
					"Hello_World_ÄÖÜ_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
