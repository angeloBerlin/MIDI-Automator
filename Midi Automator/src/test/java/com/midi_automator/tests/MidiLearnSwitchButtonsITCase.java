package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiLearnSwitchButtonsITCase extends IntegrationTestCase {

	private String deviceName;
	private String deviceScreenshot;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 106;
	private int value = 127;

	public MidiLearnSwitchButtonsITCase() {
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
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			SikuliXAutomations.cancelMidiLearnMainScreen("next_inactive.png", null,
					null);
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
	public void midiShouldBeUnlearnedOnNextButton() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn
			SikuliXAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			SikuliXAutomations.midiUnlearnMainScreen("next.png", null, null);

			// check for inactive menu item
			SikuliXAutomations
					.openPopupMenu("next.png", null, null, LOW_SIMILARITY);
			SikuliXAutomations.checkResult("midi_unlearn_inactive.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			try {
				SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
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
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			SikuliXAutomations.focusMidiAutomator();
			SikuliXAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
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

	@Test
	public void midiLearnShouldBeCanceledOnPrevButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			SikuliXAutomations.cancelMidiLearnMainScreen("prev_inactive.png", null,
					null);
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
	public void midiLearnShouldBeInactiveOnPrevButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations
					.openPopupMenu("prev.png", null, null, LOW_SIMILARITY);
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
	public void midiLearnShouldBeInactiveOnNextButton() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations
					.openPopupMenu("next.png", null, null, LOW_SIMILARITY);
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
	public void midiShouldBeUnlearnedOnPrevButton() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			SikuliXAutomations.openMidiAutomator();

			// midi learn
			SikuliXAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			SikuliXAutomations.midiUnlearnMainScreen("prev.png", null, null);

			// check for inactive menu item
			SikuliXAutomations.openPopupMenu("prev.png", "prev_inactive.png", null,
					LOW_SIMILARITY);
			SikuliXAutomations.checkResult("midi_unlearn_inactive.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			try {
				SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
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
				SikuliXAutomations.closeMidiAutomator();
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
			SikuliXAutomations.openMidiAutomator();

			SikuliXAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			SikuliXAutomations.focusMidiAutomator();
			SikuliXAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
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

	// @Test
	public void MidiShouldBeLearnedOnPrevButtonManual() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			SikuliXAutomations.focusMidiAutomator();
			// GUIAutomations.openMidiAutomator();

			SikuliXAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);
			SikuliXAutomations.focusMidiAutomator();
			SikuliXAutomations.midiLearnMainScreen("prev.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			SikuliXAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");

		} catch (FindFailed /* | IOException */e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			// try {
			// GUIAutomations.closeMidiAutomator();
			// } catch (FindFailed e) {
			// e.printStackTrace();
			// }
		}
	}
}
