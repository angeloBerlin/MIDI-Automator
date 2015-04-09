package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestOpenMidiAutomator extends SikuliAutomation {

	@Test
	public void programShallBeOpened() {

		try {
			GUIAutomations.openMidiAutomator();

			// check if opened
			GUIAutomations.checkResult("midi_automator.png");
		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
