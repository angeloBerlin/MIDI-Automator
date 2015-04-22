package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.GUIAutomations;

public class TestOpenMidiAutomator extends GUITest {

	@Test
	public void programShallBeOpened() {

		try {
			GUIAutomations.openMidiAutomator();

			// check if opened
			GUIAutomations.checkResult("midi_automator.png");
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
