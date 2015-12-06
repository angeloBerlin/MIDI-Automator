package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import java.awt.Color;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.fixture.FrameFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiMetronomFunctionalITCase extends GUITestCase {

	private String deviceName;
	private int messageType = ShortMessage.NOTE_ON;
	private int channel = 16;
	private String value1stClick = "A";
	private String valueClick = "E";
	private int octave = 4;
	private int velocity = 127;
	private int clicks = 20;
	private int clickPause = 110; // in ms, do not decrease as LoopBe fill
									// recognize it as false loop

	public MidiMetronomFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
		}
	}

	@Test
	public void nothing() {
		// TODO: Remove as soon as at least one test is active
	}

	// @Test
	public void metronomFirstClickShouldBeShown() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			FrameFixture preferencesFrame = openPreferences();
			setMidiInMetronomDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			Thread.sleep(1000);

			for (int i = 0; i < clicks; i++) {

				MidiUtils.sendMidiMessage(deviceName, messageType, channel,
						value1stClick, octave, velocity);

				// TODO: EDT problem?
				getFileList().background().requireEqualTo(Color.RED);
				Thread.sleep(clickPause);
			}
		} catch (InvalidMidiDataException | MidiUnavailableException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void metronomOtherClickShouldBeShown() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			FrameFixture preferencesFrame = openPreferences();
			setMidiInMetronomDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			Thread.sleep(1000);

			for (int i = 0; i < clicks; i++) {

				MidiUtils.sendMidiMessage(deviceName, messageType, channel,
						valueClick, octave, velocity);

				// TODO: EDT problem?
				getFileList().background().requireEqualTo(Color.RED);
				Thread.sleep(clickPause);
			}
		} catch (InvalidMidiDataException | MidiUnavailableException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}
}
