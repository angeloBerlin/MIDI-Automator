package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;

public class OpenMidiAutomatorITCase extends IntegrationTestCase {

	@Test
	public void programShallBeOpened() {

		try {
			SikuliXAutomations.openMidiAutomator();

			// check if opened
			SikuliXAutomations.checkResult("midi_automator.png");
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
