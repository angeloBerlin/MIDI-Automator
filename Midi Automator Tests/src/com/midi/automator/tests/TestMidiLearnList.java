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

public class TestMidiLearnList extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel1 = 1;
	private int channel2 = 2;
	private int controlNo = 108;
	private int value = 127;

	private int masterMessageType = ShortMessage.CONTROL_CHANGE;
	private int masterChannel = 1;
	private int masterControlNo = 102;

	public TestMidiLearnList() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			deviceScreenshot = "Bus_1.png";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			deviceScreenshot = "LoopBe_Internal_MIDI.png";
		}
	}

	@Test
	public void midiLearnShouldBeCanceled() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			GUIAutomations.restartMidiAutomator();

			// midi learn
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);

			// cancel midi learn
			GUIAutomations.cancelMidiLearnMainScreen(
					"Hello_World_1_entry_learn.png", null, null);
		} catch (FindFailed | IOException e) {
			fail(e.toString());
		}
	}

	@Test
	public void midiShouldBeUnlearned() {
		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			if (System.getProperty("os.name").equals("Mac OS X")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINBus_1.properties");
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINLoopBe_Internal_MIDI.properties");
			}
			GUIAutomations.restartMidiAutomator();

			// midi learn
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearnMainScreen(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");

			// check for inactive menu item
			GUIAutomations.openPopupMenu("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png", LOW_SIMILARITY);
			SikuliAutomation.setMinSimilarity(MAX_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_unlearn_inactive.png", MAX_TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			if (GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed(
						"Hello World 1.rtf opened though midi was unlearned.");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiShouldBeLearned() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			GUIAutomations.restartMidiAutomator();

			// set MIDI IN Remote device
			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// midi learn
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			GUIAutomations.midiLearnMainScreen(
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry.png",
					"Hello_World_2_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);

			// open files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png")) {
				throw new FindFailed("Hello World 2.rtf did not open");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiMasterLearnShouldBeRejected() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			if (System.getProperty("os.name").equals("Mac OS X")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINBus_1.properties");
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINLoopBe_Internal_MIDI.properties");
			}
			GUIAutomations.restartMidiAutomator();

			// midi learn master signature
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry.png",
					"Hello_World_2_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, masterMessageType,
					masterChannel, masterControlNo, 0);

			// check failure
			SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "error_midi_master_sig_learned.png",
					MAX_TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			// open first file by master midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, masterMessageType,
					masterChannel, masterControlNo, 0);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiDuplicateLearnShouldBeRejected() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			if (System.getProperty("os.name").equals("Mac OS X")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINBus_1.properties");
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				MockUpUtils
						.setMockupPropertiesFile("mockups/RemoteINLoopBe_Internal_MIDI.properties");
			}
			GUIAutomations.restartMidiAutomator();

			// midi learn same signature twice
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry.png",
					"Hello_World_2_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			GUIAutomations.midiLearnMainScreen(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png", DEFAULT_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);

			// check failure
			SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "error_midi_learn_already_used.png",
					MAX_TIMEOUT);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			match.highlight(HIGHLIGHT_DURATION);

			// open second file by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png")) {
				throw new FindFailed("Hello World 2.rtf did not open");
			}

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}
