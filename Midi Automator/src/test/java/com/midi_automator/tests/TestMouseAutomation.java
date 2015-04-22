package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MidiUtils;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.tests.utils.SikuliAutomation;

public class TestMouseAutomation extends GUITest {

	private String deviceName;
	private String propertiesCancelAutomation;
	private String propertiesMidiOpenerAutomation;
	private String propertiesHelloWorldAutomation;
	private String propertiesPopupAndCancelAutomation;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public TestMouseAutomation() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesCancelAutomation = "automation_cancel_always_left_Mac.properties";
			propertiesMidiOpenerAutomation = "automation_main_frame_midi_left_Mac.properties";
			propertiesHelloWorldAutomation = "automation_hello_world_1_midi_left_Mac.properties";
			propertiesPopupAndCancelAutomation = "automation_popup_and_cancel_Mac.properties";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
			propertiesMidiOpenerAutomation = "automation_main_frame_midi_left_Windows.properties";
			propertiesHelloWorldAutomation = "automation_hello_world_1_midi_left_Windows.properties";
			propertiesPopupAndCancelAutomation = "automation_popup_and_cancel_Windows.properties";
		}
	}

	@Test
	public void newAutomationShouldBeAdded() {
		try {

			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openPreferences();
			GUIAutomations.addAutomation();

			// search new automation
			GUIAutomations.checkResult("automation_empty.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
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
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openPreferences();
			GUIAutomations.deleteAutomation(1);

			// search empty automations
			GUIAutomations.checkResult("automations_list_empty.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
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
					+ propertiesCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.openMidiAutomator();

			// check if add dialog was canceled
			GUIAutomations.openAddDialog();
			GUIAutomations.checkResult("midi_automator.png");

			// check if add dialog was canceled
			GUIAutomations.openAddDialog();
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

	@Test
	public void addDialogShouldBeCanceledOnce() {
		String error = "Automation was run more than once.";

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.openMidiAutomator();

			// set trigger to once
			GUIAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once.png", 1);

			// check if add dialog was canceled
			GUIAutomations.openAddDialog();
			GUIAutomations.checkResult("midi_automator_Hello_World_12a.png");

			// check if add dialog was not canceled again
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12a.png", error);

			// cancel open dialog
			GUIAutomations.cancelDialog();

			// check if add dialog was not canceled after opening
			GUIAutomations.nextFile();
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12a.png", error);

			// cancel open dialog
			GUIAutomations.cancelDialog();

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

	@Test
	public void addDialogShouldBeCanceledOncePerOpening() {
		String error = "Automation was run more than once per opening.";

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.openMidiAutomator();

			// set trigger to once per opening
			GUIAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once_per_opening.png", 1);

			// check if add dialog was canceled before opening
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12a.png", error);

			GUIAutomations.cancelDialog();

			// check if add dialog was canceled after opening
			GUIAutomations.nextFile();
			GUIAutomations.openAddDialog();
			GUIAutomations.checkResult("midi_automator_Hello_World_12a.png");

			// check if add dialog was canceled twice after opening
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12a.png", error);
			GUIAutomations.cancelDialog();

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

	@Test
	public void automationMidiLearnShouldBeCanceled() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesCancelAutomation);
			GUIAutomations.openMidiAutomator();

			// cancel midi learn
			GUIAutomations.openPreferences();
			GUIAutomations.midiLearnAutomation(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GUIAutomations.cancelMidiLearnAutomation(1);

			// check for empty midi message
			GUIAutomations
					.checkResult("automation_midi_message_empty_active_row1.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.saveDialog();
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void addDialogShouldBeCanceledOnceByMidi() {
		String error = "Automation fired without midi trigger.";

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.openMidiAutomator();

			// open preferences
			GUIAutomations.openPreferences();

			// midi learn automation
			GUIAutomations.midiLearnAutomation(1);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check for learned midi message
			GUIAutomations.checkResult(
					"channel_1_CONTROL_CHANGE_109_value.png", 0.9f);

			GUIAutomations.saveDialog();
			GUIAutomations.focusMidiAutomator();

			// check if add dialog was canceled by some other trigger
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult("midi_automator.png", error);

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if add dialog was canceled
			GUIAutomations.checkResult("midi_automator.png");

		} catch (FindFailed | IOException | InvalidMidiDataException
				| MidiUnavailableException | InterruptedException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void addDialogShallBeCanceledWithDelay() {
		int delay = 10000;
		String error = "Automation was triggered before delay.";

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.openMidiAutomator();

			// open preferences
			GUIAutomations.openPreferences();

			// set delay
			GUIAutomations.setAutomationsTextField("automation_delay.png",
					new Integer(delay).toString(), 1);
			GUIAutomations.saveDialog();
			GUIAutomations.focusMidiAutomator();

			// check if add dialog was canceled before delay
			GUIAutomations.openAddDialog();
			GUIAutomations.checkIfNoResult("midi_automator.png", error);

			// check if add dialog was canceled
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

	@Test
	public void popUpMenuShouldBeOpenedOnce() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiOpenerAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.openMidiAutomator();

			Region mainFrame = GUIAutomations.findMidiAutomatorRegion();

			// set trigger to once right click
			GUIAutomations.openPreferences();
			GUIAutomations.setAutomationsComboBox("automation_type.png",
					"right_click.png", 1);
			GUIAutomations.setAutomationsComboBox("automation_trigger.png",
					"once.png", 1);
			GUIAutomations.saveDialog();

			// check if popup menu appears
			SikuliAutomation.setSearchRegion(mainFrame);
			GUIAutomations.checkResult("popup_menu_empty_file_list.png");

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

	@Test
	public void fileShouldBeOpenedByDoubleClickOnce() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesHelloWorldAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.openMidiAutomator();

			// set trigger to double click once
			GUIAutomations.openPreferences();
			GUIAutomations.setAutomationsComboBox("automation_type.png",
					"double_click.png", 1);
			GUIAutomations.setAutomationsComboBox("automation_trigger.png",
					"once.png", 1);
			GUIAutomations.saveDialog();

			// check if popup menu appears
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

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

	@Test
	public void delaySpinnerShouldNotSpinBelow0() {

		try {
			// mockup
			MockUpUtils
					.setMockupPropertiesFile("mockups/automation1_empty.properties");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openPreferences();
			GUIAutomations.activateAutomationsTextField("automation_delay.png",
					1);

			// spin up two times
			GUIAutomations.spinUp();
			GUIAutomations.spinUp();

			// check for delay = 2
			GUIAutomations.checkResult("automation_delay_2_active.png");

			// spin down three times
			GUIAutomations.spinDown();
			GUIAutomations.spinDown();
			GUIAutomations.spinDown();

			// check for delay = 0
			GUIAutomations.checkResult("automation_delay_0_active.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void delayShouldNotTakeDumbValues() {

		try {
			// mockup
			MockUpUtils
					.setMockupPropertiesFile("mockups/automation1_empty.properties");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.openPreferences();
			GUIAutomations.activateAutomationsTextField("automation_delay.png",
					1);

			// set delay negative
			GUIAutomations.setAutomationsTextField("automation_delay.png",
					"-1000", 1);
			GUIAutomations.saveDialog();
			GUIAutomations.openPreferences();
			GUIAutomations.checkResult("automation_delay_0.png");

			// set delay nonsense
			GUIAutomations.setAutomationsTextField("automation_delay.png",
					"/*$%%%Ghg12", 1);
			GUIAutomations.saveDialog();
			GUIAutomations.openPreferences();
			GUIAutomations.checkResult("automation_delay_0.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void multipleAutomationsShouldBeRun() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesPopupAndCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.openMidiAutomator();

			// check if dialogs are always canceled
			GUIAutomations.openAddDialog();
			GUIAutomations.checkResult("midi_automator_Hello_World_12a.png");

			// check if popup menu is opened after file opening
			GUIAutomations.openEntryByDoubleClick("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");
			GUIAutomations.checkResult("popup_menu_file_list.png");
			GUIAutomations.focusMidiAutomator();

			// check if dialogs are always canceled
			GUIAutomations.openAddDialog();
			GUIAutomations.checkResult("midi_automator_Hello_World_12a.png");

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
