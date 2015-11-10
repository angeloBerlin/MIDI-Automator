package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;
import org.sikuli.script.Region;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.tests.utils.SikuliAutomation;
import com.midi_automator.utils.MidiUtils;

public class GUIAutomationITCase extends IntegrationTestCase {

	private String deviceName;
	private String propertiesAlwaysCancelAutomation;
	private String propertiesMidiCancelAutomation;
	private String propertiesMidiMainFrameAutomation;
	private String propertiesMidiFullMainFrameAutomation;
	private String propertiesMidiHelloWorldAutomation;
	private String propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation;

	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public GUIAutomationITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Mac.properties";
			propertiesMidiMainFrameAutomation = "automation_main_frame_midi_left_Mac.properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Mac.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Mac.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Mac.properties";
			propertiesMidiFullMainFrameAutomation = "automation_midi_automator_midi_left_Mac.properties";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
			propertiesMidiMainFrameAutomation = "automation_main_frame_midi_left_Windows.properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Windows.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Windows.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Windows.properties";
		}
	}

	@Test
	public void minMaxSimilarMidiAutomatorShouldBeClicked() {

		try {
			// GUIAutomations.focusMidiAutomator();

			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiFullMainFrameAutomation);
			MockUpUtils.setMockupMidoFile("mockups/full_list.mido");
			SikuliXAutomations.openMidiAutomator();

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// search clicked Midi Automator
			SikuliXAutomations
					.checkResult("midi_automator_Hello_World_3_active.png");

			// change view of Midi Automator
			SikuliXAutomations.moveUpEntry("Hello_World_3_entry_active.png",
					"Hello_World_3_entry.png",
					"Hello_World_3_entry_inactive.png");

			// search clicked Midi Automator
			try {
				SikuliXAutomations
						.checkResult("midi_automator_Neon_Farben_active.png");
				fail("Automation found unsimilar image.");
			} catch (FindFailed e) {
			}

			// decrease similarity
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.setAutomationsTextField("automation_similarity.png",
					"0,5", 1);
			SikuliXAutomations.saveDialog();
			SikuliXAutomations.focusMidiAutomator();

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// search clicked Midi Automator
			SikuliXAutomations.checkResult("midi_automator_Neon_Farben_active.png");

		} catch (FindFailed | InvalidMidiDataException
				| MidiUnavailableException | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void newAutomationShouldBeAdded() {
		try {

			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openPreferences();
			SikuliXAutomations.addAutomation();

			// search new automation
			SikuliXAutomations.checkResult("automation_empty.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openPreferences();
			SikuliXAutomations.deleteAutomation(1);

			// search empty automations
			SikuliXAutomations.checkResult("automations_list_empty.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				// cancel preferences
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// check if add dialog was canceled
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator.png");

			// check if add dialog was canceled
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// set trigger to once
			SikuliXAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once.png", 1);

			// check if add dialog was canceled
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator_Hello_World_12i.png");

			// check if add dialog was not canceled again
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12i.png", error);

			// cancel open dialog
			SikuliXAutomations.cancelDialog();

			// check if add dialog was not canceled after opening
			SikuliXAutomations.nextFile();
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12i.png", error);

			// cancel open dialog
			SikuliXAutomations.cancelDialog();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// set trigger to once per opening
			SikuliXAutomations.setAndSaveAutomationsComboBox(
					"automation_trigger.png", "once_per_opening.png", 1);

			// check if add dialog was canceled before opening
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12i.png", error);
			SikuliXAutomations.cancelDialog();

			// check if add dialog was canceled after opening
			SikuliXAutomations.nextFile();
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator_Hello_World_12i.png");

			// check if add dialog was canceled twice after opening
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult(
					"midi_automator_Hello_World_12i.png", error);
			SikuliXAutomations.cancelDialog();

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesMidiCancelAutomation);
			SikuliXAutomations.openMidiAutomator();

			// cancel midi learn
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.midiLearnAutomation(1);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SikuliXAutomations.cancelMidiLearnAutomation(1);

			// check for empty midi message
			SikuliXAutomations
					.checkResult("automation_midi_message_empty_active_row1.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.saveDialog();
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesMidiCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// open preferences
			SikuliXAutomations.openPreferences();

			// midi learn automation
			SikuliXAutomations.midiLearnAutomation(1);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check for learned midi message
			SikuliXAutomations.checkResult(
					"channel_1_CONTROL_CHANGE_109_value.png", 0.9f);

			SikuliXAutomations.saveDialog();
			SikuliXAutomations.focusMidiAutomator();

			// check if add dialog was canceled by some other trigger
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult("midi_automator.png", error);

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if add dialog was canceled
			SikuliXAutomations.checkResult("midi_automator.png");

		} catch (FindFailed | IOException | InvalidMidiDataException
				| MidiUnavailableException | InterruptedException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// open preferences
			SikuliXAutomations.openPreferences();

			// set delay
			SikuliXAutomations.setAutomationsTextField("automation_delay.png",
					new Integer(delay).toString(), 1);
			SikuliXAutomations.saveDialog();
			SikuliXAutomations.focusMidiAutomator();

			// check if add dialog was canceled before delay
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkIfNoResult("midi_automator.png", error);

			// check if add dialog was canceled
			SikuliXAutomations.checkResult("midi_automator.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesMidiMainFrameAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			SikuliXAutomations.openMidiAutomator();

			Region mainFrame = SikuliXAutomations.findMidiAutomatorRegion();

			// set trigger to once right click
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.setAutomationsComboBox("automation_type.png",
					"right_click.png", 1);
			SikuliXAutomations.setAutomationsComboBox("automation_trigger.png",
					"once.png", 1);
			SikuliXAutomations.saveDialog();

			// check if popup menu appears
			SikuliAutomation.setSearchRegion(mainFrame);
			SikuliXAutomations.checkResult("popup_menu_empty_file_list.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
					+ propertiesMidiHelloWorldAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// set trigger to double click once
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.setAutomationsComboBox("automation_type.png",
					"double_click.png", 1);
			SikuliXAutomations.setAutomationsComboBox("automation_trigger.png",
					"once.png", 1);
			SikuliXAutomations.saveDialog();

			// check if popup menu appears
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openPreferences();
			SikuliXAutomations.activateAutomationsTextField("automation_delay.png",
					1);

			// spin up two times
			SikuliXAutomations.spinUp();
			SikuliXAutomations.spinUp();

			// check for delay = 2
			SikuliXAutomations.checkResult("automation_delay_2_active.png");

			// spin down three times
			SikuliXAutomations.spinDown();
			SikuliXAutomations.spinDown();
			SikuliXAutomations.spinDown();

			// check for delay = 0
			SikuliXAutomations.checkResult("automation_delay_0_active.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void delayShouldNotTakeInvalidValues() {

		try {
			// mockup
			MockUpUtils
					.setMockupPropertiesFile("mockups/automation1_empty.properties");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.openPreferences();
			SikuliXAutomations.activateAutomationsTextField("automation_delay.png",
					1);

			// set delay negative
			SikuliXAutomations.setAutomationsTextField("automation_delay.png",
					"-1000", 1);
			SikuliXAutomations.saveDialog();
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.checkResult("automation_delay_0.png");

			// set delay nonsense
			SikuliXAutomations.setAutomationsTextField("automation_delay.png",
					"/*$%%%Ghg12", 1);
			SikuliXAutomations.saveDialog();
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.checkResult("automation_delay_0.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.cancelDialog();
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void multipleAutomationsShouldBeRun() {

		try {
			// mockup
			MockUpUtils
					.setMockupPropertiesFile("mockups/"
							+ propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			SikuliXAutomations.openMidiAutomator();

			// check if dialogs are always canceled
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator_Hello_World_12i.png");

			// check if popup menu is opened after file opening
			SikuliXAutomations.openEntryByDoubleClick("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");
			SikuliXAutomations.checkResult("popup_menu_file_list.png");
			SikuliXAutomations.focusMidiAutomator();

			// check if dialogs are always canceled
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator_Hello_World_12i.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void movableVsNonMovableAutomation() {

		try {
			// mockup
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");

			SikuliXAutomations.openMidiAutomator();

			// check if add dialog was canceled unmoved
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator.png");

			// move cancel button
			SikuliXAutomations.dragElement(new Location(500, 200),
					"Midi_Automator_title.png",
					"Midi_Automator_title_inactive.png");
			SikuliXAutomations.focusMidiAutomator();

			// check if dialog was not canceled
			SikuliXAutomations.openAddDialog();
			try {
				SikuliXAutomations.checkResult("midi_automator.png");
				fail("Automation was done, though image was moved");
			} catch (FindFailed e) {
				SikuliXAutomations.cancelDialog();
			}

			// activate movable
			SikuliXAutomations.openPreferences();
			SikuliXAutomations.clickAutomationCheckBox("Movable.png");
			SikuliXAutomations.saveDialog();

			// check if add dialog was canceled unmoved
			SikuliXAutomations.focusMidiAutomator();
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator.png");

			// move cancel button
			SikuliXAutomations.dragElement(new Location(300, 100),
					"Midi_Automator_title.png",
					"Midi_Automator_title_inactive.png");
			SikuliXAutomations.focusMidiAutomator();

			// check if add dialog was canceled unmoved
			SikuliXAutomations.openAddDialog();
			SikuliXAutomations.checkResult("midi_automator.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
