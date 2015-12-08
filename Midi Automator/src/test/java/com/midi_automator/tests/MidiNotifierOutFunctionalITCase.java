package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.clickNotifierSendButton;
import static com.midi_automator.tests.utils.GUIAutomations.openEntryByDoubleClick;
import static com.midi_automator.tests.utils.GUIAutomations.openPreferences;
import static com.midi_automator.tests.utils.GUIAutomations.saveDialog;
import static com.midi_automator.tests.utils.GUIAutomations.setMidiOutNotifierDevice;
import static org.junit.Assert.fail;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.assertj.swing.fixture.FrameFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiNotifierOutFunctionalITCase extends GUITestCase {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String receivedSignature;

	public MidiNotifierOutFunctionalITCase() {
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
	public void notifierMessageShouldBeSentAtOpen() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set notifier out
			FrameFixture preferencesFrame = openPreferences();
			setMidiOutNotifierDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			// open entry
			openEntryByDoubleClick(0);

			// check if midi master message was sent
			Thread.sleep(1000);

			if (!"channel 1: CONTROL CHANGE 103 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for index 0.");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void notifierMessageShouldBeSentByButton() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set notifier out
			FrameFixture preferencesFrame = openPreferences();
			setMidiOutNotifierDevice(deviceName, preferencesFrame);

			// hit send button
			clickNotifierSendButton(preferencesFrame);

			// check if midi master message was sent
			Thread.sleep(1000);
			if (!"channel 1: CONTROL CHANGE 103 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature + " is wrong signature for notifier.");
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
			// TODO Auto-generated method stub
		}
	}
}
