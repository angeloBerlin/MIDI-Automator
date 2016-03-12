package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.assertj.swing.fixture.FrameFixture;
import org.junit.Test;

import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;

public class MidiRemoteOutFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String receivedSignature;
	private int remoteTimeout = 3000;

	public MidiRemoteOutFunctionalITCase() {
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
	public void masterOutMessageShouldBeSent() {

		try {

			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			FrameFixture preferencesFrame = openPreferences();
			setMidiOutRemoteDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			openEntryByDoubleClick(0);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			Thread.sleep(remoteTimeout);

			if (!"channel 1: CONTROL CHANGE 102 value: 0"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for index 0.");
			}

			openEntryByDoubleClick(1);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			Thread.sleep(remoteTimeout);
			if (!"channel 1: CONTROL CHANGE 102 value: 1"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for index 1.");
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
