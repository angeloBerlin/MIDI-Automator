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

public class TestMidiLearnNext extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 106;
	private int value = 127;

	public TestMidiLearnNext() {
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
			GUIAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			GUIAutomations.cancelMidiLearnMainScreen("next_inactive.png", null,
					null);
		} catch (FindFailed e) {
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
			GUIAutomations.midiLearnMainScreen("next.png", null, null,
					LOW_SIMILARITY);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
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
