package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MidiUtils;
import com.midi.automator.tests.utils.MockUpUtils;

public class TestMidiLearnSwitchButtons extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 106;
	private int value = 127;

	public TestMidiLearnSwitchButtons() {
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
	public void midiLearnShouldBeCanceledOnNextButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			GUIAutomations.cancelMidiLearnMainScreen("next_inactive.png", null,
					null);
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
	public void midiShouldBeUnlearnedOnNextButton() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			GUIAutomations.openMidiAutomator();

			// midi learn
			GUIAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearnMainScreen("next.png", null, null);

			// check for inactive menu item
			GUIAutomations
					.openPopupMenu("next.png", null, null, LOW_SIMILARITY);
			GUIAutomations.checkResult("midi_unlearn_inactive.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			try {
				GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
						"Hello_World_1_RTF_inactive.png");
				fail("Hello World 1.rtf opened though midi was unlearned.");
			} catch (FindFailed e) {

			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiShouldBeLearnedOnNextButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			GUIAutomations.focusMidiAutomator();
			GUIAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiLearnShouldBeCanceledOnPrevButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.openMidiAutomator();
			GUIAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			GUIAutomations.cancelMidiLearnMainScreen("prev_inactive.png", null,
					null);
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
	public void midiShouldBeUnlearnedOnPrevButton() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			GUIAutomations.openMidiAutomator();

			// midi learn
			GUIAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearnMainScreen("prev.png", null, null);

			// check for inactive menu item
			GUIAutomations.openPopupMenu("prev.png", "prev_inactive.png", null,
					LOW_SIMILARITY);
			GUIAutomations.checkResult("midi_unlearn_inactive.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			try {
				GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
						"Hello_World_1_RTF_inactive.png");
				fail("Hello World 1.rtf opened though midi was unlearned.");
			} catch (FindFailed e) {

			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void MidiShouldBeLearnedOnPrevButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.openMidiAutomator();

			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			GUIAutomations.focusMidiAutomator();
			GUIAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
