package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.MidiUtils;

public class TestMidiLearnList extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel1 = 1;
	private int channel2 = 2;
	private int controlNo = 108;
	private int value = 127;

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

	// @Test
	public void midiLearnShouldBeCanceled() {
		try {
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.midiLearn("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");
			GUIAutomations.cancelMidiLearn("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	// @Test
	public void midiShouldBeUnlearned() {
		try {
			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");

			// midi learn
			GUIAutomations.midiLearn("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearn("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");

			// check for inactive menu item
			GUIAutomations.openPopupMenu("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");
			SikuliAutomation.setMinSimilarity(MAX_SIMILARITY);
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "midi_unlearn_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);
			SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			if (GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
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
			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// midi learn
			GUIAutomations.midiLearn("Hello_World_1_entry_active.png",
					"Hello_World_1_entry.png",
					"Hello_World_1_entry_inactive.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);

			GUIAutomations.midiLearn("Hello_World_2_entry_active.png",
					"Hello_World_2_entry.png",
					"Hello_World_2_entry_inactive.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);

			// open files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel1,
					controlNo, value);
			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel2,
					controlNo, value);
			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
				throw new FindFailed("Hello World 2.rtf did not open");
			}

		} catch (FindFailed e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		} finally {
			// cleanup
			try {
				GUIAutomations.focusMidiAutomator();
				GUIAutomations.setPreferencesComboBox(
						"combo_box_midi_remote_in.png", "-none-.png");
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
