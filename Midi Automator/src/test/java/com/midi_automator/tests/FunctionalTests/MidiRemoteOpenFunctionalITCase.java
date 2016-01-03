package com.midi_automator.tests.FunctionalTests;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiRemoteOpenFunctionalITCase extends FunctionalITCase {

	private String deviceName;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 102;

	public MidiRemoteOpenFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesFile = "RemoteINBus_1.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesFile = "RemoteINLoopBe_Internal_MIDI.properties";
		}
	}

	@Test
	public void filesShouldBeOpened() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
			startApplication();

			// open files by learned midi master message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 0);
			Thread.sleep(1000);

			// check if file opened
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 1);
			Thread.sleep(1000);

			// check if file opened
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}
