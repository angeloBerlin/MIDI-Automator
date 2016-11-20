package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiNotifierOutFunctionalITCase extends FunctionalBaseCase {

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
			DialogFixture preferencesDialog = openPreferences();
			setMidiOutNotifierDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			// open entry
			openEntryByDoubleClick(0);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
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
			DialogFixture preferencesDialog = openPreferences();
			setMidiOutNotifierDevice(deviceName, preferencesDialog);

			// hit send button
			clickNotifierSendButton(preferencesDialog);

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
		}
	}
}
