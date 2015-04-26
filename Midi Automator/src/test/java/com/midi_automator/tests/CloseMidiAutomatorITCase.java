package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.utils.SystemUtils;

public class CloseMidiAutomatorITCase extends IntegrationTestCase {

	@Test
	public void programShallBeClosed() {

		try {
			GUIAutomations.openMidiAutomator();
			GUIAutomations.openExitMenu();
			Thread.sleep(3000);

			// check if process was killed
			if (System.getProperty("os.name").equals("Mac OS X")) {

				String[] cmd = { "/bin/sh", "-c",
						"ps -ax | grep \"Midi Automator\"" };
				String output = SystemUtils.runShellCommand(cmd);

				if (output.contains("Midi Automator.app")) {
					fail("Process still active");
				}
			}

			if (System.getProperty("os.name").equals("Windows 7")) {

				String[] cmd = { "cmd", "/c", "wmic process list" };
				String output = SystemUtils.runShellCommand(cmd);

				if (output.contains("Midi Automator\\jre\\bin\\javaw.exe")) {
					fail("Process still active");
				}
			}

		} catch (FindFailed | InterruptedException | IOException e) {
			fail(e.toString());
		}
	}
}
