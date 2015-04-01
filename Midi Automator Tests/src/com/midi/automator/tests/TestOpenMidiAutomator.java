package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestOpenMidiAutomator extends SikuliAutomation {

	@Test
	public void programShallBeOpened() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.openMidiAutomator();
		} catch (IOException e) {
			fail(e.toString());
		}

		// check if opened
		try {
			Region match = SCREEN.wait(screenshotpath + "midi_automator.png",
					MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
