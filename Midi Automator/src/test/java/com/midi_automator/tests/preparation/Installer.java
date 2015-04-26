package com.midi_automator.tests.preparation;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi_automator.tests.IntegrationTestCase;
import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.SikuliAutomation;

/**
 * Tests the installer and prepares the integration tests.
 * 
 * @author aguelle
 *
 */
public class Installer extends IntegrationTestCase {

	@Test
	public void midiOpenerShouldBeInstalled() {
		try {
			GUIAutomations.openMidiAutomatorInstaller();
			Region match = SikuliAutomation.getSearchRegion();

			// MacOS
			if (System.getProperty("os.name").equals("Mac OS X")) {

				match.dragDrop(screenshotpath + "Midi_automator_app_icon.png",
						screenshotpath + "Applications_icon.png");
				match = screen.wait(screenshotpath + "ersetzen_button.png",
						MAX_TIMEOUT);
				match.click();
				Thread.sleep(5000);
			}

			GUIAutomations.closeMidiAutomatorInstaller();
		} catch (IOException | FindFailed | InterruptedException e) {
			fail(e.toString());
		}
	}
}
