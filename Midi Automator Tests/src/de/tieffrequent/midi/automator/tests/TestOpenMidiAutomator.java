package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

public class TestOpenMidiAutomator extends SikuliTest {

	@Test
	public void test() {
		String filePath = "";

		if (System.getProperty("os.name").equals("Mac OS X")) {
			filePath = "/Applications/Midi Automator.app";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			filePath = "%PROGRAMFILES%\\MidiAutomator\\Midi Automator.exe";
		}

		File file = new File(filePath);

		try {

			Desktop.getDesktop().open(file);
			Region match = SCREEN.wait(screenshotpath + "midi_automator.png",
					TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (IOException e) {
			fail(e.toString());
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

}
