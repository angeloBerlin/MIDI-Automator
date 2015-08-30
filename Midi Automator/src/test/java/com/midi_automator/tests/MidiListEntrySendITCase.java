package com.midi_automator.tests;

import static org.junit.Assert.fail;

import java.io.IOException;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.junit.Test;
import org.sikuli.script.FindFailed;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiListEntrySendITCase extends IntegrationTestCase {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String deviceScreenshot;
	private String receivedSignature;

	public MidiListEntrySendITCase() {
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
	public void midiMessageShouldBeSentAtOpen() {

		try {
			// mockup
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			GUIAutomations.openMidiAutomator();
			GUIAutomations.setAndSavePreferencesComboBox(
					"combo_box_switch_list_entry_out.png", deviceScreenshot);

			// open entry 1
			GUIAutomations.openEntryByDoubleClick("Hello_World_1_entry.png",
					"Hello_World_1_entry_active.png",
					"Hello_World_1_entry_inactive.png");

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 16: CONTROL CHANGE 1 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// open entry 2
			GUIAutomations.openEntryByDoubleClick("Hello_World_2_entry.png",
					"Hello_World_2_entry_active.png",
					"Hello_World_2_entry_inactive.png");

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 16: CONTROL CHANGE 2 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
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
