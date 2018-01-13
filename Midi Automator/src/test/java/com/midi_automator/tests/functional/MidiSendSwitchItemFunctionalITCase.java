package com.midi_automator.tests.functional;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;

public class MidiSendSwitchItemFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private MidiDevice device;
	private Receiver receiver;
	private String receivedSignature;
	private String midiOutSwitchItemDeviceProperties;

	public MidiSendSwitchItemFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			midiOutSwitchItemDeviceProperties = "mockups/MidiOUTSwitchItemDeviceBus_1.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			midiOutSwitchItemDeviceProperties = "mockups/MidiOUTSwitchItemDeviceLoopBe_Internal_Midi.properties";
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
			DialogFixture preferencesDialog = openPreferences();
			setMidiOutSwitchItemDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			// open entry 1
			openEntryByDoubleClick(0);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 1 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// open entry 2
			openEntryByDoubleClick(1);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 2 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiMessageShouldBeSentByMenu() {

		try {
			MockUpUtils
					.setMockupMidoFile("mockups/Hello_World_123_empty_3_no_send.mido");
			MockUpUtils
					.setMockupPropertiesFile(midiOutSwitchItemDeviceProperties);
			startApplication();

			// send entry 1
			JPopupMenuFixture popupMenu = openFileListPopupMenu(0);
			popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_SEND_MIDI)
					.click();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 1 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// send entry 2
			popupMenu = openFileListPopupMenu(1);
			popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_SEND_MIDI)
					.click();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 2 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// send entry 3 should be disabled
			popupMenu = openFileListPopupMenu(2);
			popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_SEND_MIDI)
					.requireDisabled();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiMessageShouldBeSentByKeyStroke() {

		try {
			MockUpUtils
					.setMockupMidoFile("mockups/Hello_World_123_empty_3_no_send.mido");
			MockUpUtils
					.setMockupPropertiesFile(midiOutSwitchItemDeviceProperties);
			startApplication();

			// send entry 1
			selectEntryByLeftClick(0);
			pressKeyOnMainFrame(KeyEvent.VK_ALT);
			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_M);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 1 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			// send entry 2
			selectEntryByLeftClick(1);
			pressKeyOnMainFrame(KeyEvent.VK_ALT);
			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_M);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if midi master message was sent
			if (!"channel 16: CONTROL CHANGE 2 value: 127"
					.equals(receivedSignature)) {
				fail(receivedSignature
						+ " is wrong master signature for entry 1.");
			}

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
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
