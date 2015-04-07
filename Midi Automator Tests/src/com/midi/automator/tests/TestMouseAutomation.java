package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MidiUtils;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestMouseAutomation extends GUITest {

	private String deviceName;
	private String propertiesMidiMockup;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public TestMouseAutomation() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesMidiMockup = "automation_cancel_always_left_Bus1.properties";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesMidiMockup = "automation_cancel_always_left_LoopBe1"
					+ ".properties";
		}
	}

	@Test
	public void newAutomationShouldBeAdded() {
		try {

			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			GUIAutomations.restartMidiAutomator();

			// open preferences
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());

			// add automation
			GUIAutomations.addAutomation();

			// search new entry
			Region match = SikuliAutomation.getSearchRegion().wait(
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
			GUIAutomations.deleteAutomation(1);

			// search new entry
			Region match = SikuliAutomation.getSearchRegion().wait(
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

	@Test
	public void addDialogShouldAlwaysBeCanceled() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open add dialog
			GUIAutomations.openAddDialog();

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			// open add dialog
			GUIAutomations.openAddDialog();

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void addDialogShouldBeCanceledOnce() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.restartMidiAutomator();

			// set trigger to once
			GUIAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once.png", 1);

			// open add dialog
			GUIAutomations.openAddDialog();

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator_Hello_World_12a.png",
					MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			// open add dialog
			GUIAutomations.openAddDialog();

			// check if add dialog was not canceled
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + "midi_automator_Hello_World_12a.png",
						DEFAULT_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
				fail("Automation was run more than once.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}

			// cancel open dialog
			GUIAutomations.cancelDialog();

			// Try automation after opening
			GUIAutomations.nextFile();
			GUIAutomations.openAddDialog();

			// check if add dialog was not canceled
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + "midi_automator_Hello_World_12a.png",
						DEFAULT_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
				fail("Automation was run more than once.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}

			// cancel open dialog
			GUIAutomations.cancelDialog();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void addDialogShouldBeCanceledOncePerOpening() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.restartMidiAutomator();

			// set trigger to once per opening
			GUIAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once_per_opening.png", 1);

			GUIAutomations.openAddDialog();

			// check if add dialog was not canceled
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				Region match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + "midi_automator_Hello_World_12a.png",
						DEFAULT_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
				fail("Automation was run more than once.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}
			GUIAutomations.cancelDialog();

			// open next file
			GUIAutomations.nextFile();
			GUIAutomations.openAddDialog();

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator_Hello_World_12a.png",
					MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			GUIAutomations.openAddDialog();

			// check if add dialog was not canceled
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + "midi_automator_Hello_World_12a.png",
						DEFAULT_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
				fail("Automation was run more than once.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}
			GUIAutomations.cancelDialog();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void automationMidiLearnShouldBeCanceled() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			GUIAutomations.restartMidiAutomator();

			// cancel midi learn
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());
			GUIAutomations.midiLearnAutomation(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GUIAutomations.cancelMidiLearnAutomation(1);

			// check for empty midi message
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath
							+ "automation_midi_message_empty_active_row1.png",
					MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.saveDialog();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void addDialogShouldBeCanceledOnceByMidi() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open preferences
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());

			// midi learn automation
			GUIAutomations.midiLearnAutomation(1);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check for learned midi message
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "channel_1_CONTROL_CHANGE_109_value.png",
					MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			GUIAutomations.saveDialog();
			GUIAutomations.focusMidiAutomator();
			GUIAutomations.openAddDialog();

			// check if add dialog was not canceled by some other trigger
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + "midi_automator.png.png", MAX_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
				fail("No midi trigger fired yet.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InvalidMidiDataException | MidiUnavailableException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShallBeCanceledWithDelay() {
		int delay = 10000;

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMockup);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// open preferences
			SikuliAutomation.setSearchRegion(GUIAutomations.openPreferences());

			// set delay
			GUIAutomations.setAutomationsTextField("automation_delay.png", new Integer(
					delay).toString(), 1);
			GUIAutomations.saveDialog();
			GUIAutomations.focusMidiAutomator();

			GUIAutomations.openAddDialog();
			// check if add dialog was canceled before delay
			try {
				setMinSimilarity(HIGH_SIMILARITY);
				SikuliAutomation.getSearchRegion()
						.wait(screenshotpath + "midi_automator.png",
								delay / 1000 - 1);
				fail("Automation trigger delay is not over yet.");
			} catch (FindFailed e) {

			} finally {
				setMinSimilarity(DEFAULT_SIMILARITY);
			}

			// check if add dialog was canceled
			setMinSimilarity(HIGH_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", MAX_TIMEOUT);
			setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}
}
