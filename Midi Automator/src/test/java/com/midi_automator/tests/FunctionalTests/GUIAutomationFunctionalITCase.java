package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.Point;
import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.exception.UnexpectedException;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.presenter.Messages;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

public class GUIAutomationFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String propertiesAlwaysCancelAutomation;
	private String propertiesMidiCancelAutomation;
	private String propertiesMidiFullMainFrameAutomation;
	private String propertiesMidiHelloWorldAutomation;
	private String propertiesOnceMainFrame;
	private String propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation;
	private String propertiesAutomationMidiLearned;

	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public GUIAutomationFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Mac.properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Mac.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Mac.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Mac.properties";
			propertiesMidiFullMainFrameAutomation = "automation_midi_automator_midi_left_Mac.properties";
			propertiesOnceMainFrame = "automation_main_frame_once_left_Mac.properties";
			propertiesAutomationMidiLearned = "automation_cancel_midi_learned_left_Mac.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
			propertiesMidiHelloWorldAutomation = "automation_hello_world_1_midi_left_Windows.properties";
			propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation = "automation_popup_and_cancel_Windows.properties";
			propertiesMidiCancelAutomation = "automation_cancel_midi_left_Windows.properties";
			propertiesMidiFullMainFrameAutomation = "automation_midi_automator_midi_left_Windows.properties";
			propertiesOnceMainFrame = "automation_main_frame_once_left_Windows.properties";
			propertiesAutomationMidiLearned = "automation_cancel_midi_learned_left_Windows.properties";
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
			Thread.sleep(8000);

			// search clicked Midi Automator
			getFileList().requireSelectedItems(6);

			// change view of Midi Automator
			moveUpEntry(5);

			// decrease similarity
			DialogFixture preferencesDialog = openPreferences();
			setAutomationMinSimilarity("0.5", preferencesDialog);
			saveDialog(preferencesDialog);

			Thread.sleep(1000);

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);

			Thread.sleep(5000);

			// search clicked Midi Automator
			getFileList().requireSelectedItems(7);

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

		DialogFixture preferencesDialog = openPreferences();
		addAutomation(preferencesDialog);

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		table.requireRowCount(1);
	}

	@Test
	public void automationShouldBeDeleted() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		DialogFixture preferencesDialog = openPreferences();
		deleteAutomation(0, preferencesDialog);

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(6000);
			addDialog.requireNotVisible();

			// check if add dialog was canceled again
			addDialog = openAddDialog();
			Thread.sleep(6000);
			addDialog.requireNotVisible();

			// check if add dialog was canceled again
			addDialog = openAddDialog();
			Thread.sleep(6000);
			addDialog.requireNotVisible();

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
			DialogFixture preferencesDialog = openPreferences();
			setAutomationTrigger(GUIAutomation.TRIGGER_ONCE, 0,
					preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was canceled
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// check if add dialog was not canceled again
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();
			cancelDialog(addDialog);

			// check if add dialog was not canceled after opening
			clickNextFile();
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

			cancelDialog(addDialog);

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
			DialogFixture preferencesDialog = openPreferences();
			setAutomationTrigger(GUIAutomation.TRIGGER_ONCE_PER_CHANGE, 0,
					preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was not canceled before opening
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();
			cancelDialog(addDialog);

			// check if add dialog was canceled after opening
			clickNextFile();
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// check if add dialog was canceled twice after opening
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

			cancelDialog(addDialog);

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
			DialogFixture preferencesDialog = openPreferences();
			midiLearnAutomation(0, preferencesDialog);
			Thread.sleep(1000);

			// TODO: right click on inactive component
			cancelMidiLearnAutomation(0, preferencesDialog);

			// check for empty midi message
			JTableFixture table = getGUIAutomationTable(preferencesDialog);
			int column = table
					.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

			table.requireCellValue(TableCell.row(0).column(column), "");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void automationMidiShouldBeUnlearned() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAutomationMidiLearned);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// unlearn midi
			DialogFixture preferencesDialog = openPreferences();
			midiUnLearnAutomation(0, preferencesDialog);
			Thread.sleep(1000);

			// check for empty midi message
			JTableFixture table = getGUIAutomationTable(preferencesDialog);
			int column = table
					.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

			table.requireCellValue(TableCell.row(0).column(column), "");

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
			DialogFixture preferencesDialog = openPreferences();
			midiLearnAutomation(0, preferencesDialog);
			Thread.sleep(1000);

			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(2000);

			// check for learned midi message
			JTableFixture table = getGUIAutomationTable(preferencesDialog);
			int column = table
					.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

			table.requireCellValue(TableCell.row(0).column(column),
					"channel 1: CONTROL CHANGE 109 value: 127");
			saveDialog(preferencesDialog);

			// check if add dialog was canceled by some other trigger
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(5000);

			// check if add dialog was canceled
			addDialog.requireNotVisible();

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
			DialogFixture preferencesDialog = openPreferences();

			// set delay
			setAutomationMinDelay("6000", 0, preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was canceled before delay
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

			// check if add dialog was canceled after delay
			Thread.sleep(5000);
			addDialog.requireNotVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDialogShallNotBeCanceledAfterTimeout() {

		try {

			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// open preferences
			DialogFixture preferencesDialog = openPreferences();

			// set delay
			setAutomationTimeout("10000", 0, preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was canceled before time out
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// check if add dialog was not canceled after time out
			Thread.sleep(6000);
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void popUpMenuShouldBeOpenedOnce() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesOnceMainFrame);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// set type to once right click
			DialogFixture preferencesDialog = openPreferences();
			setAutomationType(GUIAutomation.CLICKTYPE_RIGHT, 0,
					preferencesDialog);
			saveDialog(preferencesDialog);
			Thread.sleep(1000);

			// check if popup menu appears
			Thread.sleep(5000);
			JPopupMenuFixture popupMenu = new JPopupMenuFixture(robot,
					ctx.getBean(MainFramePopupMenu.class));
			popupMenu.requireVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fileShouldBeOpenedByDoubleClickOnce() {

		try {

			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiHelloWorldAutomation);
			MockUpUtils
					.setMockupMidoFile("mockups/Hello_World_12_no_file.mido");
			startApplication();

			// set trigger to double click once
			DialogFixture preferencesDialog = openPreferences();
			setAutomationType(GUIAutomation.CLICKTYPE_DOUBLE, 0,
					preferencesDialog);
			setAutomationTrigger(GUIAutomation.TRIGGER_ONCE, 0,
					preferencesDialog);
			setAutomationMinDelay("1000", 0, preferencesDialog);
			saveDialog(preferencesDialog);

			window.focus();

			// check if file was opened
			Thread.sleep(5000);

			checkInfoText(String.format(Messages.MSG_FILE_LIST_NOT_FOUND,
					"./testfiles/Hello World_no_file.rtf"));

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

		DialogFixture preferencesDialog = openPreferences();

		// spin up two times
		spinUpAutomationDelaySpinner(2, 0, preferencesDialog);

		// check for delay = 2
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		table.requireCellValue(automationsDelayCell(0, preferencesDialog), "2");

		// spin down three times
		spinDownAutomationDelaySpinner(3, 0, preferencesDialog);

		// check for delay = 0
		table.requireCellValue(automationsDelayCell(0, preferencesDialog), "0");
	}

	@Test
	public void delayShouldNotTakeInvalidValues() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		DialogFixture preferencesDialog = openPreferences();

		// set delay negative
		// change keyboard layout to EN to be sure this is working properly
		try {
			setAutomationMinDelay("-1000", 0, preferencesDialog);
		} catch (UnexpectedException e) {
		}
		saveDialog(preferencesDialog);
		preferencesDialog = openPreferences();

		// check for delay = 0
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		table.click();
		table.requireCellValue(automationsDelayCell(0, preferencesDialog), "0");

		// set delay nonsense
		try {
			setAutomationMinDelay("$%Ghg12", 0, preferencesDialog);
		} catch (UnexpectedException e) {
		}
		saveDialog(preferencesDialog);
		preferencesDialog = openPreferences();

		// check for delay = 0
		table = getGUIAutomationTable(preferencesDialog);
		table.click();
		table.requireCellValue(automationsDelayCell(0, preferencesDialog), "0");
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
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// check if popup menu is opened after file opening
			openEntryByDoubleClick(1);
			Thread.sleep(5000);
			JPopupMenuFixture popupMenu = new JPopupMenuFixture(robot,
					ctx.getBean(MainFramePopupMenu.class));
			popupMenu.requireVisible();

			// check if dialogs are always canceled
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

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
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// move cancel button
			window.moveTo(new Point(500, 200));

			// check if dialog was not canceled
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();
			cancelDialog(addDialog);

			// activate movable
			DialogFixture preferencesDialog = openPreferences();
			clickAutomationMovableCheckBox(0, preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was canceled unmoved
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// move cancel button
			window.moveTo(new Point(10, 10));

			// check if add dialog was canceled unmoved
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void scanRateShouldBeSet() {

		try {
			MockUpUtils
					.setMockupPropertiesFile("mockups/"
							+ propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			startApplication();

			// check if add dialog was canceled within 5 seconds
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// set scan rate to 0.1
			DialogFixture preferencesDialog = openPreferences();
			setAutomationScanRate("0.1", 0, preferencesDialog);
			clickAutomationMovableCheckBox(0, preferencesDialog);
			saveDialog(preferencesDialog);

			// check if add dialog was canceled after 10 seconds
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void storedAutomationsShouldBeActivatedAfterpreferencesDialogWasCanceled() {

		try {
			MockUpUtils
					.setMockupPropertiesFile("mockups/"
							+ propertiesOncePerOpeningHelloWorld1PopupAndAlwaysCancelAutomation);
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
			startApplication();

			// check if add dialog was canceled
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

			// open and cancel preferences
			DialogFixture preferencesDialog = openPreferences();
			cancelDialog(preferencesDialog);

			// check if add dialog was canceled
			addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireNotVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void screenshotChooserShouldRememberLastDirectory() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		startApplication();

		DialogFixture preferencesDialog = openPreferences();
		JFileChooserFixture fileChooser = openScreenshotFileChooser(0,
				preferencesDialog);

		// choose a file
		fileChooser.setCurrentDirectory(new File(currentPath + File.separator
				+ "testfiles"));
		String cancelButtonImage1 = currentPath + File.separator + "testfiles"
				+ File.separator + "cancel_button.png";
		File cancelButtonImageFile1 = new File(cancelButtonImage1);
		fileChooser.selectFile(cancelButtonImageFile1);
		fileChooser.approve();

		saveDialog(preferencesDialog);

		// re-select file
		preferencesDialog = openPreferences();
		fileChooser = openScreenshotFileChooser(0, preferencesDialog);

		String cancelButtonImage2 = currentPath + File.separator + "testfiles"
				+ File.separator + "cancel_button2.png";
		File cancelButtonImageFile2 = new File(cancelButtonImage2);
		fileChooser.selectFile(cancelButtonImageFile2);
		fileChooser.approve();

		if (!sikulix.checkforStates("cancel_button2.png")) {
			fail();
		}
	}

	@Test
	public void screenshotShouldBeRemoved() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesAlwaysCancelAutomation);
			startApplication();

			DialogFixture preferencesDialog = openPreferences();
			removeScreenshotFromAutomation(0, preferencesDialog);
			saveDialog(preferencesDialog);

			// check if screenshot was removed
			DialogFixture addDialog = openAddDialog();
			Thread.sleep(5000);
			addDialog.requireVisible();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
