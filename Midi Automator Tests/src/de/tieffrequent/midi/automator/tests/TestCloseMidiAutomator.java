package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automaotr.tests.utils.Utils;

public class TestCloseMidiAutomator extends SikuliTest {

	@Test
	public void programShallBeClosed() {

		Region match;

		try {
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);

			// open file menu
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "file.png", TIMEOUT);
			match.click();

			// exit
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "exit.png", TIMEOUT);
			match.click();

		} catch (FindFailed e) {
			fail(e.toString());
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// check if process was killed
		if (System.getProperty("os.name").equals("Mac OS X")) {

			String[] cmd = { "/bin/sh", "-c",
					"ps -ax | grep \"Midi Automator\"" };
			String output = Utils.runShellCommand(cmd);

			if (output.contains("Midi Automator.app")) {
				fail(output);
			}
		}

		if (System.getProperty("os.name").equals("Windows 7")) {

		}
	}
}
