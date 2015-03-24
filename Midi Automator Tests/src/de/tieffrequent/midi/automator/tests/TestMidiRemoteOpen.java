package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import de.tieffrequent.midi.automator.tests.utils.GUIAutomations;
import de.tieffrequent.midi.automator.tests.utils.MidiUtils;

public class TestMidiRemoteOpen extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 102;

	public TestMidiRemoteOpen() {
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
	public void filesShouldBeOpened() {

		try {
			// set MIDI IN Remote device
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_remote_in.png", deviceScreenshot);

			// add files
			GUIAutomations.addFile("Hello World 1", currentPath
					+ "/testfiles/Hello World 1.rtf");
			GUIAutomations.addFile("Hello World 2", currentPath
					+ "/testfiles/Hello World 2.rtf");

			// open files by learned midi master message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 0);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png")) {
				throw new FindFailed("Hello World 1.rtf did not open");
			}
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 1);
			if (!GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png")) {
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
				GUIAutomations.setPreferencesComboBox(
						"combo_box_midi_remote_in.png", "-none-.png");
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}
}
