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

public class TestMidiRemoteOpen extends GUITest {

	private String deviceName;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 102;

	public TestMidiRemoteOpen() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
		}
	}

	@Test
	public void filesShouldBeOpened() {

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

			// open files by learned midi master message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 0);
			GUIAutomations.checkIfFileOpened("Hello_World_1_RTF.png",
					"Hello_World_1_RTF_inactive.png");
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, 1);
			GUIAutomations.checkIfFileOpened("Hello_World_2_RTF.png",
					"Hello_World_2_RTF_inactive.png");

		} catch (FindFailed | IOException e) {
			fail(e.toString());
		} catch (InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}
