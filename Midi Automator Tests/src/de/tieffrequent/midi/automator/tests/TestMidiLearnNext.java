package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.MidiUtils;

public class TestMidiLearnNext extends GUITest {

	private String deviceName = "Bus 1";
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 106;
	private int value = 127;

	@Test
	public void midiLearnShouldBeCanceled() {

		try {
			GUIAutomations.midiLearn("next.png");
			GUIAutomations.cancelMidiLearn("next_inactive.png");
		} catch (FindFailed e) {
			fail(e.toString());
		}
	}

	@Test
	public void midiShouldBeUnlearned() {
		try {
			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", "Bus1.png");

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");

			// midi learn
			GUIAutomations.midiLearn("next.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// midi unlearn
			GUIAutomations.midiUnlearn("next.png");

			// check for inactive menu item
			GUIAutomations.openPopupMenu("next.png");
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
			GUIAutomations.restartMidiAutomator();

			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", "Bus1.png");

			// midi learn
			GUIAutomations.midiLearn("next.png");
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
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 106, 127);
			if (!GUIAutomations
					.checkIfFileOpened("Hello_World_2_RTF_inactive.png")) {
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
				GUIAutomations.focusMidiAutomator();
				GUIAutomations.midiUnlearn("next.png");
				GUIAutomations.setPreferencesComboBox(
						"combo_box_midi_remote_in.png", "-none-.png");
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
