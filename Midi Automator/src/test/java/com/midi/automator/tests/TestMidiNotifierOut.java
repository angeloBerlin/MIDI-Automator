package com.midi.automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;

import com.midi.automator.tests.utils.GUIAutomations;
import com.midi.automator.tests.utils.MidiUtils;
import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class TestMidiNotifierOut extends GUITest {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String deviceScreenshot;
	private String receivedSignature;

	public TestMidiNotifierOut() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			deviceScreenshot = "Bus_1.png";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			deviceName = "LoopBe Internal MIDI";
			deviceScreenshot = "LoopBe_Internal_MIDI.png";
		}

		try {
			device = MidiUtils.getMidiDevice(deviceName, "IN");
			device.open();
			receiver = new MidiINReceiver();
			MidiUtils.setReceiverToDevice(device, receiver);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void notifierMessageShouldBeSentAtOpen() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.openMidiAutomator();
			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_midi_notifier_out.png", deviceScreenshot);
			GUIAutomations.openEntryByDoubleClick(
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png",
					"Hello_World_1_entry.png");

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 1: CONTROL CHANGE 103 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for index 0.");
			}

		} catch (FindFailed | IOException | InterruptedException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void notifierMessageShouldBeSentByButton() {

		try {
			// mockup
			GUIAutomations.openMidiAutomator();
			GUIAutomations.setPreferencesComboBox(
					"combo_box_midi_notifier_out.png", deviceScreenshot);
			// hit send button
			Region match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "send_button.png", MAX_TIMEOUT);
			match.click();

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 1: CONTROL CHANGE 103 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature + " is wrong signature for notifier.");
			}

		} catch (FindFailed | IOException | InterruptedException e) {
			fail(e.toString());
		} finally {
			try {
				GUIAutomations.cancelDialog();
				GUIAutomations.closeMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	class MidiINReceiver implements Receiver {

		@Override
		public void send(MidiMessage message, long timeStamp) {

			receivedSignature = MidiUtils.messageToString(message);
		}

		@Override
		public void close() {
			// TODO Auto-generated method stub
		}
	}
}
