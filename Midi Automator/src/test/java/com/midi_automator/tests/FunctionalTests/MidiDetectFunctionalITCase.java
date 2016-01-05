package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.UIManager;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.frames.MainFrame;

public class MidiDetectFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String remoteINpropertiesFile;
	private String remoteOUTpropertiesFile;
	private int messageType = ShortMessage.NOTE_ON;
	private int channel = 16;
	private String value = "A";
	private int octave = 4;
	private int velocity = 127;

	public MidiDetectFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			remoteINpropertiesFile = "RemoteINBus_1.properties";
			remoteOUTpropertiesFile = "RemoteOUTBus_1.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			remoteINpropertiesFile = "RemoteINLoopBe_Internal_MIDI.properties";
			remoteOUTpropertiesFile = "RemoteOUTLoopBe_Internal_MIDI.properties";
		}
	}

	@Test
	public void midiINShouldBeShown() {

		// send midi signal double as fast as flash duration
		int sendInterval = MainFrame.MIDI_DETECT_BLINK_RATE / 2;
		int amountOfSends = 6;

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils
				.setMockupPropertiesFile("mockups/" + remoteINpropertiesFile);
		startApplication();

		try {
			Thread.sleep(1000);

			for (int i = 0; i < amountOfSends; i++) {

				Thread.sleep(sendInterval);

				MidiUtils.sendMidiMessage(deviceName, messageType, channel,
						value, octave, velocity);

				Thread.sleep(MainFrame.MIDI_DETECT_BLINK_RATE / 2);

				if (i % 2 == 0) {
					getMidiINDetect().background().requireEqualTo(
							MainFrame.MIDI_DETECT_COLOR);
				}

				Thread.sleep(MainFrame.MIDI_DETECT_BLINK_RATE);

				if (i % 2 == 0) {
					getMidiINDetect().background().requireEqualTo(
							UIManager.getColor("Panel.background"));
				}

			}
		} catch (InvalidMidiDataException | MidiUnavailableException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiOUTShouldBeShown() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/"
				+ remoteOUTpropertiesFile);
		startApplication();

		try {
			clickNextFile();
			Thread.sleep(50);
			getMidiOUTDetect().background().requireEqualTo(
					MainFrame.MIDI_DETECT_COLOR);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
