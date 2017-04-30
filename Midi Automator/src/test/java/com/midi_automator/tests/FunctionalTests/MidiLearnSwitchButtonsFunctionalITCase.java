package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.Messages;
import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.MainFrame.MainFrame;
import com.midi_automator.view.windows.menus.MidiLearnPopupMenu;

public class MidiLearnSwitchButtonsFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String propertiesFile;
	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 106;
	private int value = 127;

	public MidiLearnSwitchButtonsFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesFile = "RemoteINBus_1.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesFile = "RemoteINLoopBe_Internal_MIDI.properties";
		}
	}

	// @Test
	// Blocked due to
	// https://github.com/joel-costigliola/assertj-swing/issues/169"
	public void midiLearnShouldBeCanceledOnNextButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
		startApplication();

		// midi learn
		midiLearnNextButton();
		getNextButton().requireDisabled();

		// cancel midi learn
		cancelMidiNextButton();

		// midi learn should be shown
		JPopupMenuFixture popupMenu = openNextButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireVisible();

		getNextButton().requireEnabled();
	}

	@Test
	public void midiShouldBeUnlearnedOnNextButton() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils
					.setMockupPropertiesFile("mockups/next_prev_midi_learned.properties");
			startApplication();

			// midi unlearn
			midiUnlearnNextButton();

			// check for inactive menu item
			JPopupMenuFixture popup = openNextButtonPopupMenu();
			popup.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
					.requireDisabled();
			getFileList().click();

			// open first files by learned midi message
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 2, 127);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check info text
			checkInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					MainFrame.NAME_NEXT_BUTTON));
			// check that file was not opened
			sikulix.checkIfFileNotOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void midiShouldBeLearnedOnNextButton() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set midi in device
			DialogFixture preferencesDialog = openPreferences();
			setMidiInRemoteDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			midiLearnNextButton();
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if learned messages was displayed
			checkInfoText("Midi message <b>channel 1: CONTROL CHANGE 106 value: 127</b> learned.");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if first file was not opened
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if second file was not opened
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	// @Test
	// Blocked due to
	// https://github.com/joel-costigliola/assertj-swing/issues/169"
	public void midiLearnShouldBeCanceledOnPrevButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
		startApplication();

		// midi learn
		midiLearnPrevButton();
		getPrevButton().requireDisabled();

		// cancel midi learn
		cancelMidiPrevButton();

		// midi learn should be shown
		JPopupMenuFixture popupMenu = openPrevButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireVisible();

		getPrevButton().requireEnabled();

	}

	@Test
	public void midiLearnShouldBeInactiveOnPrevButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// check for inactive midi learn menu item
		JPopupMenuFixture popup = openNextButtonPopupMenu();
		popup.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireDisabled();
	}

	@Test
	public void midiLearnShouldBeInactiveOnNextButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// check for inactive midi learn menu item
		JPopupMenuFixture popup = openPrevButtonPopupMenu();
		popup.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.requireDisabled();
	}

	@Test
	public void midiShouldBeUnlearnedOnPrevButton() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils
					.setMockupPropertiesFile("mockups/next_prev_midi_learned.properties");
			startApplication();

			// midi unlearn
			midiUnlearnPrevButton();

			// check for inactive menu item
			JPopupMenuFixture popup = openPrevButtonPopupMenu();
			popup.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
					.requireDisabled();
			getFileList().click();

			// open first files by learned midi message
			MidiUtils.sendMidiMessage(deviceName, messageType, 1, 2, 127);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check info text
			checkInfoText(String.format(Messages.MSG_MIDI_UNLEARNED,
					MainFrame.NAME_PREV_BUTTON));
			// check that file was not opened
			sikulix.checkIfFileNotOpened("Hello_World_2_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void MidiShouldBeLearnedOnPrevButton() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// set midi in device
			DialogFixture preferencesDialog = openPreferences();
			setMidiInRemoteDevice(deviceName, preferencesDialog);
			saveDialog(preferencesDialog);

			midiLearnPrevButton();
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if learned messages was displayed
			checkInfoText("Midi message <b>channel 1: CONTROL CHANGE 106 value: 127</b> learned.");

			// open second files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if second file was not opened
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

			// open first files by learned midi message
			Thread.sleep(1000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			// check if first file was not opened
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}
