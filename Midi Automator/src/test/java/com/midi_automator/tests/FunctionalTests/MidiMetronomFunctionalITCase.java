package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.MainFrame.MainFrame;

public class MidiMetronomFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private int messageType = ShortMessage.NOTE_ON;
	private int channel = 16;
	private String value1stClick = "A";
	private String valueClick = "E";
	private int octave = 4;
	private int velocity = 127;

	public MidiMetronomFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
		}
	}

	@Test
	public void metronomBeatShouldBeShown() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			DialogFixture preferencesDialog = openPreferences();
			setMidiInMetronomDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					value1stClick, octave, velocity);

			getFileList().background().requireEqualTo(
					MainFrame.METRONOM_COLOR_FIRST_CLICK);

			Thread.sleep(500);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					valueClick, octave, velocity);

			getFileList().background().requireEqualTo(
					MainFrame.METRONOM_COLOR_OTHER_CLICK);

			Thread.sleep(500);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					value1stClick, octave, velocity);

			getFileList().background().requireEqualTo(
					MainFrame.METRONOM_COLOR_FIRST_CLICK);

		} catch (InvalidMidiDataException | MidiUnavailableException
				| InterruptedException e) {
			e.printStackTrace();
		}
	}
}
