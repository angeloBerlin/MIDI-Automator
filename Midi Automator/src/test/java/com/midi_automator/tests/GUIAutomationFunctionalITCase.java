package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import java.awt.Point;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.exception.UnexpectedException;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.automationconfiguration.ConfigurationTableModel;

public class GUIAutomationFunctionalITCase extends GUITestCase {

	private String deviceName;
	private String propertiesAlwaysCancelAutomation;
	private String propertiesMidiCancelAutomation;
	private String propertiesMidiMainFrameAutomation;
	private String propertiesMidiFullMainFrameAutomation;
	private String propertiesMidiHelloWorldAutomation;
	private String propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation;

	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public GUIAutomationFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Mac.properties";
			propertiesMidiMainFrameAutomation = "automation_main_frame_midi_left_Mac.properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Mac.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Mac.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Mac.properties";
			propertiesMidiFullMainFrameAutomation = "automation_midi_automator_midi_left_Mac.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
			propertiesMidiMainFrameAutomation = "automation_main_frame_midi_left_Windows.properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Windows.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Windows.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Windows.properties";
			propertiesMidiFullMainFrameAutomation = "automation_midi_automator_midi_left_Windows.properties";
		}
	}

	@Test
	public void minMaxSimilarMidiAutomatorShouldBeClicked() {

		MockUpUtils.setMockupPropertiesFile("mockups/"
				+ propertiesMidiFullMainFrameAutomation);
		MockUpUtils.setMockupMidoFile("mockups/full_list.mido");
		startApplication();

		try {
			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(5000);

			// search clicked Midi Automator
			getFileList().requireSelectedItems(5);

			// change view of Midi Automator
			moveUpEntry(5);
			Thread.sleep(5000);
			getFileList().requireNoSelection();

			// decrease similarity
			FrameFixture preferencesFrame = openPreferences();
			setAutomationMinSimilarity("0,5", 0, preferencesFrame);
			saveDialog(preferencesFrame);

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			Thread.sleep(5000);

			// search clicked Midi Automator
			getFileList().requireSelectedItems(5);

			resetAutomations();

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void newAutomationShouldBeAdded() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		FrameFixture preferencesFrame = openPreferences();
		addAutomation(preferencesFrame);

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.requireRowCount(1);
	}

	@Test
	public void automationShouldBeDeleted() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		FrameFixture preferencesFrame = openPreferences();
		deleteAutomation(0, preferencesFrame);

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.requireRowCount(0);
	}

	@Test
	public void addDialogShouldAlwaysBeCanceled() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// check if add dialog was canceled
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// check if add dialog was canceled again
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShouldBeCanceledOnce() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			startApplication();

			// set trigger to once
			FrameFixture preferencesFrame = openPreferences();
			setAutomationTrigger(GUIAutomation.CLICKTRIGGER_ONCE, 0,
					preferencesFrame);
			saveDialog(preferencesFrame);

			// check if add dialog was canceled
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// check if add dialog was not canceled again
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();
			cancelDialog(addFrame);

			// check if add dialog was not canceled after opening
			nextFile();
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();

			cancelDialog(addFrame);
			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShouldBeCanceledOncePerOpening() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			startApplication();

			// set trigger to once per opening
			FrameFixture preferencesFrame = openPreferences();
			setAutomationTrigger(GUIAutomation.CLICKTRIGGER_ONCE_PER_CHANGE, 0,
					preferencesFrame);
			saveDialog(preferencesFrame);

			// check if add dialog was not canceled before opening
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();
			cancelDialog(addFrame);

			// check if add dialog was canceled after opening
			nextFile();
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// check if add dialog was canceled twice after opening
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();

			cancelDialog(addFrame);
			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void automationMidiLearnShouldBeCanceled() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// cancel midi learn
			FrameFixture preferencesFrame = openPreferences();
			midiLearnAutomation(0, preferencesFrame);
			Thread.sleep(1000);

			// TODO: right click on inactive component
			cancelMidiLearnAutomation(0, preferencesFrame);

			// check for empty midi message
			JTableFixture table = getGUIAutomationTable(preferencesFrame);
			table.requireCellValue(
					TableCell
							.row(0)
							.column(ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE),
					"");

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShouldBeCanceledOnceByMidi() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// midi learn automation
			FrameFixture preferencesFrame = openPreferences();
			midiLearnAutomation(0, preferencesFrame);
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(1000);

			// check for learned midi message
			JTableFixture table = getGUIAutomationTable(preferencesFrame);
			table.requireCellValue(
					TableCell
							.row(0)
							.column(ConfigurationTableModel.COLUMN_INDEX_MIDI_SIGNATURE),
					"channel 1: CONTROL CHANGE 109 value: 127");
			saveDialog(preferencesFrame);

			// check if add dialog was canceled by some other trigger
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(5000);

			// check if add dialog was canceled
			addFrame.requireNotVisible();

			resetAutomations();

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShallBeCanceledWithDelay() {

		try {

			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// open preferences
			FrameFixture preferencesFrame = openPreferences();

			// set delay
			setAutomationMinDelay("6000", 0, preferencesFrame);
			saveDialog(preferencesFrame);

			// check if add dialog was canceled before delay
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();

			// check if add dialog was canceled after delay
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void popUpMenuShouldBeOpenedOnce() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiMainFrameAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// set trigger to once right click
			FrameFixture preferencesFrame = openPreferences();
			setAutomationType(GUIAutomation.CLICKTYPE_RIGHT, 0,
					preferencesFrame);
			setAutomationTrigger(GUIAutomation.CLICKTRIGGER_ONCE, 0,
					preferencesFrame);
			saveDialog(preferencesFrame);

			// check if popup menu appears
			Thread.sleep(5000);
			JPopupMenuFixture popupMenu = new JPopupMenuFixture(robot,
					ctx.getBean(MainFramePopupMenu.class));
			popupMenu.requireVisible();

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fileShouldBeOpenedByDoubleClickOnce() {

		try {

			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiHelloWorldAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			startApplication();

			// set trigger to double click once
			FrameFixture preferencesFrame = openPreferences();
			setAutomationType(GUIAutomation.CLICKTYPE_DOUBLE, 0,
					preferencesFrame);
			setAutomationTrigger(GUIAutomation.CLICKTRIGGER_ONCE, 0,
					preferencesFrame);
			saveDialog(preferencesFrame);

			// check if file was opened
			Thread.sleep(5000);

			checkIfEntryWasOpened("Hello World 1");
			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void delaySpinnerShouldNotSpinBelow0() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		FrameFixture preferencesFrame = openPreferences();

		// spin up two times
		spinUpAutomationDelaySpinner(2, 0, preferencesFrame);

		// check for delay = 2
		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.requireCellValue(automationsDelayCell(0), "2");

		// spin down three times
		spinDownAutomationDelaySpinner(3, 0, preferencesFrame);

		// check for delay = 0
		table.requireCellValue(automationsDelayCell(0), "0");
	}

	@Test
	public void delayShouldNotTakeInvalidValues() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		FrameFixture preferencesFrame = openPreferences();

		// set delay negative
		// change keyboard layout to EN to be sure this is working properly
		try {
			setAutomationMinDelay("-1000", 0, preferencesFrame);
		} catch (UnexpectedException e) {
		}
		saveDialog(preferencesFrame);
		preferencesFrame = openPreferences();

		// check for delay = 0
		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.click();
		table.requireCellValue(automationsDelayCell(0), "0");

		// set delay nonsense
		try {
			setAutomationMinDelay("$%Ghg12", 0, preferencesFrame);
		} catch (UnexpectedException e) {
		}
		saveDialog(preferencesFrame);
		preferencesFrame = openPreferences();

		// check for delay = 0
		table = getGUIAutomationTable(preferencesFrame);
		table.click();
		table.requireCellValue(automationsDelayCell(0), "0");

		deleteAllAutomations(preferencesFrame);
		saveDialog(preferencesFrame);
	}

	@Test
	public void multipleAutomationsShouldBeRun() {

		try {
			MockUpUtils
					.setMockupPropertiesFile("mockups/"
							+ propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			startApplication();

			// check if dialogs are always canceled
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// check if popup menu is opened after file opening
			openEntryByDoubleClick(1);
			Thread.sleep(5000);
			JPopupMenuFixture popupMenu = new JPopupMenuFixture(robot,
					ctx.getBean(MainFramePopupMenu.class));
			popupMenu.requireVisible();

			// check if dialogs are always canceled
			addFrame = openAddDialog();
			Thread.sleep(2000);
			addFrame.requireNotVisible();

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void movableVsNonMovableAutomation() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// check if add dialog was canceled unmoved
			FrameFixture addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// move cancel button
			window.moveTo(new Point(500, 200));

			// check if dialog was not canceled
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireVisible();
			cancelDialog(addFrame);

			// activate movable
			FrameFixture preferencesFrame = openPreferences();
			clickAutomationMovableCheckBox(0, preferencesFrame);
			saveDialog(preferencesFrame);

			// check if add dialog was canceled unmoved
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			// move cancel button
			window.moveTo(new Point(10, 10));

			// check if add dialog was canceled unmoved
			addFrame = openAddDialog();
			Thread.sleep(5000);
			addFrame.requireNotVisible();

			resetAutomations();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
