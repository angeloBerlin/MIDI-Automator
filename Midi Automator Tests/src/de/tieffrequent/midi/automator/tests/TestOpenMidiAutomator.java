package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestOpenMidiAutomator extends SikuliAutomation {

	@Test
	public void programShallBeOpened() {

		try {
			GUIAutomations.openMidiAutomator();
		} catch (IOException e) {
			fail(e.toString());
		}

		// check if opened
		try {
			GUIAutomations.findMidiAutomatorRegion();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
