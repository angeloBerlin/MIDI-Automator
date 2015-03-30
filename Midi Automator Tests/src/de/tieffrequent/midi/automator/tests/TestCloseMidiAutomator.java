package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.Utils;

public class TestCloseMidiAutomator extends GUITest {

	@Test
	public void programShallBeClosed() {

		try {
			GUIAutomations.openExitMenu();
		} catch (FindFailed e) {
			fail(e.toString());
		}

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// check if process was killed
		if (System.getProperty("os.name").equals("Mac OS X")) {

			String[] cmd = { "/bin/sh", "-c",
					"ps -ax | grep \"Midi Automator\"" };
			String output = Utils.runShellCommand(cmd);

			if (output.contains("Midi Automator.app")) {
				fail("Process still active");
			}
		}

		if (System.getProperty("os.name").equals("Windows 7")) {

			String[] cmd = { "cmd", "/c", "wmic process list" };
			String output = Utils.runShellCommand(cmd);

			if (output.contains("Midi Automator\\jre\\bin\\javaw.exe")) {
				fail("Process still active");
			}
		}
	}
}
