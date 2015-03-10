package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automaotr.tests.utils.Utils;

public class TestOpenMidiAutomator extends SikuliTest {

	@Test
	public void programShallBeOpened() {

		String filePath = "";

		if (System.getProperty("os.name").equals("Mac OS X")) {
			filePath = "/Applications/Midi Automator.app";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			filePath = Utils
					.replaceSystemVariables("%ProgramFiles%\\Midi Automator\\Midi Automator.exe");
		}

		File file = new File(filePath);

		try {
			Desktop.getDesktop().open(file);
			Region searchRegion = Automations.findMidiAutomatorRegion();
			searchRegion.y = searchRegion.y - 30;
			searchRegion.w = searchRegion.w + 100;
			searchRegion.h = searchRegion.h + 130;
			AllTests.setProgramRegion(searchRegion);

		} catch (IOException e) {
			fail(e.toString());
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
