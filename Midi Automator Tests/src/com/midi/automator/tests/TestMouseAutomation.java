package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestMouseAutomation extends GUITest {

	@Test
	public void newAutomationShouldBeAdded() {
		try {

			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			GUIAutomations.restartMidiAutomator();

			// open preferences
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());

			// add automation
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "add_automation_button.png", MAX_TIMEOUT);
			match.click();

			// search new entry
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "automation_empty.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				GUIAutomations.cancelDialog();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void automationShouldBeDeleted() {
		try {

			// mockup
			MockUpUtils
					.setMockupPropertiesFile("mockups/automation1_empty.properties");
			GUIAutomations.restartMidiAutomator();

			// open preferences
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());

			// delete automation
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "automation_empty.png", MAX_TIMEOUT);
			match.click();
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "delete_automation_button.png",
					MAX_TIMEOUT);
			match.click();

			// search new entry
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "automations_list_empty.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				GUIAutomations.cancelDialog();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
