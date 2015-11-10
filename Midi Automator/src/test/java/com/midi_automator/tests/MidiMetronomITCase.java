package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.SikuliXAutomations;
import com.midi_automator.tests.utils.SikuliAutomation;
import com.midi_automator.utils.MidiUtils;

public class MidiMetronomITCase extends IntegrationTestCase {

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

	public MidiMetronomITCase() {
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
		failMessage = null;

		try {
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.setAndSavePreferencesComboBox(
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
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void metronomOtherClickShouldBeShown() {

		failMessage = null;

		try {
			SikuliXAutomations.openMidiAutomator();
			SikuliXAutomations.setAndSavePreferencesComboBox(
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
		} finally {
			try {
				SikuliXAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	class SikuliWatcher extends Thread {

		String screenshot;

		public SikuliWatcher(String screenshot) {
			this.screenshot = screenshot;
		}

		public void run() {
			try {
				SikuliXAutomations.checkResult(screenshot);
			} catch (FindFailed e) {
				failMessage = e.toString();
			} finally {
				SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
			}
		}
	}
}
