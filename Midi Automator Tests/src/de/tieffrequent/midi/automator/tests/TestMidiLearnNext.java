package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.MidiUtils;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

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
			GUIAutomations.midiLearn("next.png", null, null);
			GUIAutomations.cancelMidiLearn("next_inactive.png", null, null);
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void midiShouldBeUnlearned() {
		try {
			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");

			// midi learn
			GUIAutomations.midiLearn("next.png", null, null);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearn("next.png", null, null);

			// check for inactive menu item
			GUIAutomations.openPopupMenu("next.png", null, null);
			SikuliAutomation.setMinSimilarity(MAX_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_unlearn_inactive.png", TIMEOUT);
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

		} catch (FindFailed e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			// cleanup
			try {
				SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
				GUIAutomations.setPreferencesComboBox(
						"combo_box_midi_remote_in.png", "-none-.png");
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void midiShouldBeLearned() {

		try {
			GUIAutomations.restartMidiAutomator();

			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// midi learn
			GUIAutomations.midiLearn("next.png", null, null);
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

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
		} finally {
			// cleanup
			try {
				GUIAutomations.midiUnlearn("next.png", null, null);
				GUIAutomations.setPreferencesComboBox(
						"combo_box_midi_remote_in.png", "-none-.png");
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
