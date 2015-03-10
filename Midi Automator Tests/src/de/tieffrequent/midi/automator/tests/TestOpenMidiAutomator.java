package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

public class TestOpenMidiAutomator extends SikuliTest {

	@Test
	public void programShallBeOpened() {

		try {
			Automations.openMidiAutomator();
		} catch (IOException e) {
			fail(e.toString());
		}

		// check if opened
		try {
			Automations.findMidiAutomatorRegion();

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
