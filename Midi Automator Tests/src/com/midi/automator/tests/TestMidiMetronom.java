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

public class TestMidiMetronom extends GUITest {

	private String deviceName;
	private String deviceScreenshot;
	private int messageType = ShortMessage.NOTE_ON;
	private int channel = 16;
	private String value1stClick = "A";
	private String valueClick = "E";
	private int octave = 4;
	private int velocity = 127;
	private String failMessage = null;
	private int clicks = 20;
	private int clickPause = 110; // in ms, do not decrease as LoopBe fill
									// recognize it as false loop

	public TestMidiMetronom() {
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
	public void metronomFirstClickShouldBeShown() {

		try {
			failMessage = null;

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// set MIDI Metronom IN device
			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_metronom_in.png", deviceScreenshot);

			Thread.sleep(500);

			// start watching for 1st click metronom
			SikuliWatcher watcher = new SikuliWatcher(
					"metronom_first_click.png");
			watcher.start();

			for (int i = 0; i < clicks; i++) {
				MidiUtils.sendMidiMessage(deviceName, messageType, channel,
						value1stClick, octave, velocity);
				Thread.sleep(clickPause);
			}

			// wait for watcher thread
			while (watcher.isAlive()) {
			}

			// check if test has failed
			if (failMessage != null) {
				fail(failMessage);
			}

		} catch (FindFailed e) {
			fail(e.toString());
		} catch (IOException | InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void metronomOtherClickShouldBeShown() {

		try {
			failMessage = null;

			// mockup
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			GUIAutomations.restartMidiAutomator();

			// set MIDI Metronom IN device
			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_metronom_in.png", deviceScreenshot);

			Thread.sleep(500);

			// start watching for second click metronom
			SikuliWatcher watcher = new SikuliWatcher(
					"metronom_other_click.png");
			watcher.start();

			for (int i = 0; i < clicks; i++) {
				MidiUtils.sendMidiMessage(deviceName, messageType, channel,
						valueClick, octave, velocity);
				Thread.sleep(clickPause);
			}

			// wait for watcher thread
			while (watcher.isAlive()) {
			}

			// check if test has failed
			if (failMessage != null) {
				fail(failMessage);
			}

		} catch (FindFailed e) {
			fail(e.toString());
		} catch (IOException | InterruptedException | MidiUnavailableException
				| InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	class SikuliWatcher extends Thread {

		String screenshot;

		public SikuliWatcher(String screenshot) {
			this.screenshot = screenshot;
		}

		public void run() {
			try {
				SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
				Region match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + screenshot, DEFAULT_TIMEOUT);
				match.highlight(HIGHLIGHT_DURATION);
			} catch (FindFailed e) {
				failMessage = e.toString();
			} finally {
				SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			}
		}
	}
}
