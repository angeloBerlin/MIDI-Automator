package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.cancelMidiNextButton;
import static com.midi_automator.tests.utils.GUIAutomations.cancelMidiPrevButton;
import static com.midi_automator.tests.utils.GUIAutomations.getFileList;
import static com.midi_automator.tests.utils.GUIAutomations.midiLearnNextButton;
import static com.midi_automator.tests.utils.GUIAutomations.midiLearnPrevButton;
import static com.midi_automator.tests.utils.GUIAutomations.midiUnlearnNextButton;
import static com.midi_automator.tests.utils.GUIAutomations.midiUnlearnPrevButton;
import static com.midi_automator.tests.utils.GUIAutomations.openNextButtonPopupMenu;
import static com.midi_automator.tests.utils.GUIAutomations.openPreferences;
import static com.midi_automator.tests.utils.GUIAutomations.openPrevButtonPopupMenu;
import static com.midi_automator.tests.utils.GUIAutomations.saveDialog;
import static com.midi_automator.tests.utils.GUIAutomations.setMidiInRemoteDevice;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.MidiLearnPopupMenu;

public class MidiLearnSwitchButtonsFunctionalITCase extends GUITestCase {

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
	public void midiLearnShouldBeCanceledOnNextButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
		startApplication();

		midiLearnNextButton();

		// cancel midi learn
		// TODO: not possible as JList is disabled
		cancelMidiNextButton();

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
			Thread.sleep(1000);

			// check that file was not opened
			checkEmptyInfoText();

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
			FrameFixture preferencesFrame = openPreferences();
			setMidiInRemoteDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			midiLearnNextButton();
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open first files by learned midi message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if first file was not opened
			checkIfEntryWasOpened("Hello World 1");

			// open second files by learned midi message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if second file was not opened
			checkIfEntryWasOpened("Hello World 2");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void midiLearnShouldBeCanceledOnPrevButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/" + propertiesFile);
		startApplication();

		midiLearnPrevButton();

		// cancel midi learn
		// TODO: not possible as JList is disabled
		cancelMidiPrevButton();
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
			Thread.sleep(1000);

			// check that file was not opened
			checkEmptyInfoText();

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
			FrameFixture preferencesFrame = openPreferences();
			setMidiInRemoteDevice(deviceName, preferencesFrame);
			saveDialog(preferencesFrame);

			midiLearnPrevButton();
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// open second files by learned midi message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if second file was not opened
			checkIfEntryWasOpened("Hello World 2");

			// open first files by learned midi message
			Thread.sleep(2000);
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			// check if first file was not opened
			checkIfEntryWasOpened("Hello World 1");

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}
