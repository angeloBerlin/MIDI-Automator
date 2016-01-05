package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.assertj.swing.fixture.FrameFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiSendSwitchItemFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String receivedSignature;

	public MidiSendSwitchItemFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
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
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set midi switch list entry out device
			FrameFixture preferencesFrame = openPreferences();
			setMidiOutSwitchItemDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			// open entry 1
			openEntryByDoubleClick(0);

			// check if midi master message was sent
			Thread.sleep(1000);

			if (!"channel 16: CONTROL CHANGE 1 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// open entry 2
			openEntryByDoubleClick(1);

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 16: CONTROL CHANGE 2 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class MidiINReceiver implements Receiver {

		@Override
		public void send(MidiMessage message, long timeStamp) {

			receivedSignature = MidiUtils.messageToString(message);
		}

		@Override
		public void close() {
		}
	}
}
