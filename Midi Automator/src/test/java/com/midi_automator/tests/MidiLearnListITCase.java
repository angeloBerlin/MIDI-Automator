package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.tests.utils.SikuliAutomation;
import com.midi_automator.utils.MidiUtils;

public class MidiLearnListITCase extends IntegrationTestCase {

	private String deviceName;
	private String deviceScreenshot;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel1 = 1;
	private int channel2 = 2;
	private int controlNo = 108;
	private int value = 127;

	private int masterMessageType = ShortMessage.CONTROL_CHANGE;
	private int masterChannel = 1;
	private int masterControlNo = 102;

	public MidiLearnListITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			deviceScreenshot = "Bus_1.png";
			propertiesFile = "RemoteINBus_1.properties";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			deviceScreenshot = "LoopBe_Internal_MIDI.png";
			propertiesFile = "RemoteINLoopBe_Internal_MIDI.properties";
		}
	}

	@Test
	public void midiLearnShouldBeCanceled() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn
			SikuliXAutomations.midiLearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);

			// cancel midi learn
			SikuliXAutomations.cancelMidiLearnMainScreen(
					"Hello_World_1_entry_learn.png", null, null);
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
	public void midiLearnShouldBeInactive() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			// midi learn
			SikuliXAutomations.midiLearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);

			SikuliXAutomations.checkResult("midi_learn_inactive.png");
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
	public void midiShouldBeUnlearned() {

		String error = "File was opened though midi message was unlearned.";

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn
			SikuliXAutomations.midiLearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			// midi unlearn
			SikuliXAutomations.midiUnlearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png");

			// check for inactive menu item
			SikuliXAutomations.openPopupMenu("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);
			SikuliXAutomations.checkResult("midi_unlearn_inactive.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);

			try {
				SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
						"Hello_World_1_RTF_inactive.png");
				fail(error);
			} catch (FindFailed e) {

			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiShouldBeLearned() {

		Settings.CheckLastSeen = false;

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			SikuliXAutomations.focusMidiAutomator();
			SikuliXAutomations.midiLearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", HIGH_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			SikuliXAutomations.midiLearnMainScreen("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png", HIGH_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);

			// open files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
				Settings.CheckLastSeen = SikuliAutomation.CHECK_LAST_SEEN;
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiMasterLearnShouldBeRejected() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn master signature
			SikuliXAutomations.midiLearnMainScreen("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, masterMessageType,
					masterChannel, masterControlNo, 0);

			// check failure
			SikuliXAutomations.checkResult("error_midi_master_sig_learned.png");

			// open first file by master midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, masterMessageType,
					masterChannel, masterControlNo, 0);
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiDuplicateLearnShouldBeRejected() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn same signature twice
			SikuliXAutomations.midiLearnMainScreen("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			SikuliXAutomations.midiLearnMainScreen("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);

			// check failure
			SikuliXAutomations.checkResult("error_midi_learn_already_used.png");

			// open second file by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
