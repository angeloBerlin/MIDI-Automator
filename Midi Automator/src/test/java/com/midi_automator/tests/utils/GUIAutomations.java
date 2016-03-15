package com.midi_automator.tests.utils;

import java.awt.Point;
import java.io.IOException;

import javax.swing.JSpinner;

import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.finder.JFileChooserFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JLabelFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.assertj.swing.fixture.JSpinnerFixture;
import org.assertj.swing.fixture.JTableCellFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.sikuli.script.FindFailed;

import com.midi_automator.utils.SystemUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.MidiLearnPopupMenu;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.frames.AddFrame;
import com.midi_automator.view.frames.EditFrame;
import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.frames.PreferencesFrame;

public class GUIAutomations {

	public static FrameFixture window;
	public static Robot robot;

	/**
	 * Opens the Midi Automator program
	 * 
	 * @throws IOException
	 * @throws FindFailed
	 */
	public static void openMidiAutomator() throws IOException {

		String[] command = null;

		if (System.getProperty("os.name").equals("Mac OS X")) {
			command = new String[] { "open", "-n",
					"/Applications/Midi Automator.app" };
		}

		if (System.getProperty("os.name").contains("Windows")) {
			command = new String[] {
					SystemUtils
							.replaceSystemVariables("%PROGRAMFILES%\\Midi Automator\\Midi Automator.exe"),
					"-test" };
		}

		SystemUtils.runShellCommand(command);
		focusMidiAutomator();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Puts the program to midi learn for the "next" button.
	 * 
	 */
	public static void midiLearnNextButton() {
		JPopupMenuFixture popupMenu = openNextButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Unlearns a midi message on the "next" button.
	 * 
	 */
	public static void midiUnlearnNextButton() {
		JPopupMenuFixture popupMenu = openNextButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
				.click();
	}

	/**
	 * Cancels the midi learn mode on "next" button.
	 */
	public static void cancelMidiNextButton() {
		JPopupMenuFixture popupMenu = openNextButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Puts the program to midi learn for the "previous" button.
	 * 
	 */
	public static void midiLearnPrevButton() {
		JPopupMenuFixture popupMenu = openPrevButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Unlearns a midi message on the "previous" button.
	 * 
	 */
	public static void midiUnlearnPrevButton() {
		JPopupMenuFixture popupMenu = openPrevButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
				.click();
	}

	/**
	 * Cancels the midi learn mode on "previous" button.
	 */
	public static void cancelMidiPrevButton() {
		JPopupMenuFixture popupMenu = openPrevButtonPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Puts the program to midi learn for the given list entry index.
	 * 
	 * @param index
	 *            The index of the list entry
	 */
	public static void midiLearnListEntry(int index) {
		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Unlearns a midi message on the given list entry index.
	 * 
	 * @param index
	 *            The index of the list entry
	 */
	public static void midiUnlearnListEntry(int index) {
		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
				.click();
	}

	/**
	 * Cancels the midi learn mode on the file list
	 */
	public static void cancelMidiLearnListEntry() {
		JPopupMenuFixture popupMenu = openFileListPopupMenu();
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void midiLearnAutomation(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_LEARN)
				.click();
	}

	/**
	 * Un-learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void midiUnLearnAutomation(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
				.click();
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void cancelMidiLearnAutomation(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItemWithPath(
				MidiLearnPopupMenu.MENU_ITEM_MIDI_LEARN_CANCEL).click();
	}

	/**
	 * Deletes an automation by row number
	 * 
	 * @param row
	 *            The row number to delete
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void deleteAutomation(int row, FrameFixture preferencesFrame) {
		getGUIAutomationTable(preferencesFrame).selectRows(row);
		preferencesFrame.button(
				GUIAutomationConfigurationPanel.NAME_DELETE_BUTTON).click();
	}

	/**
	 * Deletes all automations
	 * 
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void deleteAllAutomations(FrameFixture preferencesFrame) {

		int rowCount = getGUIAutomationTable(preferencesFrame).rowCount();

		for (int i = 0; i < rowCount; i++) {
			deleteAutomation(0, preferencesFrame);
		}
	}

	/**
	 * Deletes all active automations
	 */
	public static void resetAutomations() {
		FrameFixture preferencesFrame = openPreferences();
		deleteAllAutomations(preferencesFrame);
		saveDialog(preferencesFrame);
	}

	/**
	 * Adds an empty automation
	 * 
	 * @param preferencesFrame
	 *            The preferences Frame
	 */
	public static void addAutomation(FrameFixture preferencesFrame) {
		preferencesFrame
				.button(GUIAutomationConfigurationPanel.NAME_ADD_BUTTON)
				.click();
	}

	/**
	 * Opens the preferences window.
	 * 
	 * @return The preferences window
	 */
	public static FrameFixture openPreferences() {

		window.menuItemWithPath(MainFrame.MENU_FILE,
				MainFrame.MENU_ITEM_PREFERENCES).click();
		return WindowFinder.findFrame(PreferencesFrame.NAME).using(robot);
	}

	/**
	 * Opens the import window.
	 * 
	 * @return The file chooser
	 */
	public static JFileChooserFixture openImportDialog() {
		window.menuItemWithPath(MainFrame.MENU_FILE, MainFrame.MENU_ITEM_IMPORT)
				.click();
		return JFileChooserFinder.findFileChooser().using(robot);
	}

	/**
	 * Opens the export window.
	 * 
	 * @return The file chooser
	 */
	public static JFileChooserFixture openExportDialog() {
		window.menuItemWithPath(MainFrame.MENU_FILE, MainFrame.MENU_ITEM_EXPORT)
				.click();
		return JFileChooserFinder.findFileChooser().using(robot);
	}

	/**
	 * Sets the MIDI IN remote device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setMidiInRemoteDevice(String value,
			FrameFixture preferencesFrame) {
		setPreferencesComboBox(
				PreferencesFrame.NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX, value,
				preferencesFrame);
	}

	/**
	 * Sets the MIDI OUT list entry device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setMidiOutSwitchItemDevice(String value,
			FrameFixture preferencesFrame) {
		setPreferencesComboBox(
				PreferencesFrame.NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX,
				value, preferencesFrame);
	}

	/**
	 * Sets the MIDI IN metronom device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setMidiInMetronomDevice(String value,
			FrameFixture preferencesFrame) {
		setPreferencesComboBox(
				PreferencesFrame.NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX,
				value, preferencesFrame);
	}

	/**
	 * Sets the MIDI OUT notifier device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setMidiOutNotifierDevice(String value,
			FrameFixture preferencesFrame) {
		setPreferencesComboBox(
				PreferencesFrame.NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX,
				value, preferencesFrame);
	}

	/**
	 * Sets the MIDI OUT remote device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setMidiOutRemoteDevice(String value,
			FrameFixture preferencesFrame) {
		setPreferencesComboBox(
				PreferencesFrame.NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX, value,
				preferencesFrame);
	}

	/**
	 * Clicks the switch notifier send button
	 * 
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void clickNotifierSendButton(FrameFixture preferencesFrame) {
		preferencesFrame.button(PreferencesFrame.NAME_BUTTON_SEND_NOTIFIER)
				.click();
	}

	/**
	 * Sets the preferences combo box to the given value
	 * 
	 * @param name
	 *            The name of the combo box
	 * @param value
	 *            The value of the combo box
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void setPreferencesComboBox(String name, String value,
			FrameFixture preferencesFrame) {
		preferencesFrame.comboBox(name).selectItem(value);
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationsComboBox(String value, int row,
			int column, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.enterValue(TableCell.row(row).column(column), value);
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param columnName
	 *            The name of the column
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationsComboBox(String value, int row,
			String columnName, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		table.enterValue(
				TableCell.row(row).column(table.columnIndexFor(columnName)),
				value);
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationTrigger(String value, int row,
			FrameFixture preferencesFrame) {

		setAutomationsComboBox(value, row,
				GUIAutomationConfigurationTable.COLNAME_TRIGGER,
				preferencesFrame);
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationType(String value, int row,
			FrameFixture preferencesFrame) {
		setAutomationsComboBox(value, row,
				GUIAutomationConfigurationTable.COLNAME_TYPE, preferencesFrame);
	}

	/**
	 * Spins up the automation delay spinner
	 * 
	 * @param times
	 *            Amount of times to spin up
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void spinUpAutomationDelaySpinner(int times, int row,
			FrameFixture preferencesFrame) {

		spinUpAutomationsSpinner(times, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesFrame);
	}

	/**
	 * Spins down the automation delay spinner
	 * 
	 * @param times
	 *            Amount of times to spin down
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void spinDownAutomationDelaySpinner(int times, int row,
			FrameFixture preferencesFrame) {

		spinDownAutomationsSpinner(times, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesFrame);
	}

	/**
	 * Gets the coordinates of the table cell of the automations min delay
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesFrame
	 *            The preferences frame
	 * @return The TableCell
	 */
	public static TableCell automationsDelayCell(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIN_DELAY);
		return TableCell.row(row).column(column);
	}

	/**
	 * Spins up an automation spinner
	 * 
	 * @param times
	 *            Amount of times to spin up
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void spinUpAutomationsSpinner(int times, int row, int column,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.doubleClick();

		JSpinnerFixture spinnerEditor = getAutomationsSpinner(row, column,
				preferencesFrame);

		spinnerEditor.increment(times);
		table.click();
	}

	/**
	 * Spins up an automation spinner
	 * 
	 * @param times
	 *            Amount of times to spin up
	 * @param row
	 *            The row of the automation
	 * @param columnName
	 *            The name of the column
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void spinUpAutomationsSpinner(int times, int row,
			String columnName, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		spinUpAutomationsSpinner(times, row, table.columnIndexFor(columnName),
				preferencesFrame);
	}

	/**
	 * Spins down a automation spinner
	 * 
	 * @param times
	 *            Amount of times to spin down
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferences frame
	 */
	public static void spinDownAutomationsSpinner(int times, int row,
			int column, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.doubleClick();

		JSpinnerFixture spinnerEditor = getAutomationsSpinner(row, column,
				preferencesFrame);

		spinnerEditor.decrement(times);
		table.click();
	}

	/**
	 * Spins down an automation spinner
	 * 
	 * @param times
	 *            Amount of times to spin up
	 * @param row
	 *            The row of the automation
	 * @param columnName
	 *            The name of the column
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void spinDownAutomationsSpinner(int times, int row,
			String columnName, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		spinDownAutomationsSpinner(times, row,
				table.columnIndexFor(columnName), preferencesFrame);
	}

	/**
	 * Gets a Spinner from the automation table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferences frame
	 * @return
	 */
	public static JSpinnerFixture getAutomationsSpinner(int row, int column,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		JSpinnerFixture spinnerEditor = new JSpinnerFixture(robot,
				(JSpinner) cell.editor());

		return spinnerEditor;
	}

	/**
	 * Gets the delay spinner from the automation table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesFrame
	 *            The preferences frame
	 * @return
	 */
	public static JSpinnerFixture getAutomationsDelaySpinner(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);

		int index = table
				.columnIndexFor(table
						.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIN_DELAY));

		return getAutomationsSpinner(row, index, preferencesFrame);
	}

	/**
	 * Clicks a check box in the automations table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferences Frame
	 */
	public static void clickAutomationCheckBox(int row, int column,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.click();
	}

	/**
	 * Clicks a check box in the automations table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesFrame
	 *            The preferences Frame
	 */
	public static void clickAutomationMovableCheckBox(int row,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MOVABLE);
		clickAutomationCheckBox(row, column, preferencesFrame);
	}

	/**
	 * Gets the automation table from the preferences frame
	 * 
	 * @param preferencesFrame
	 *            The preferences frame
	 * @return The GUI automation table
	 */
	public static JTableFixture getGUIAutomationTable(
			FrameFixture preferencesFrame) {
		return preferencesFrame.table(GUIAutomationConfigurationTable.NAME);
	}

	/**
	 * Sets a mouse automation spinner option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationsSpinner(String value, int row, int column,
			FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		JSpinnerFixture spinnerEditor = new JSpinnerFixture(robot,
				(JSpinner) cell.editor());

		cell.doubleClick();
		spinnerEditor.enterTextAndCommit(value);
		table.click();
	}

	/**
	 * Sets a mouse automation spinner option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param columnName
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationsSpinner(String value, int row,
			String columnName, FrameFixture preferencesFrame) {

		JTableFixture table = getGUIAutomationTable(preferencesFrame);
		setAutomationsSpinner(value, row, table.columnIndexFor(columnName),
				preferencesFrame);
	}

	/**
	 * Sets a mouse automation minimum similarity
	 * 
	 * @param value
	 *            The value to choose
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationMinSimilarity(String value,
			FrameFixture preferencesFrame) {

		JSpinnerFixture spinner = preferencesFrame
				.spinner(GUIAutomationConfigurationPanel.NAME_MIN_SIMILARITY_SPINNER);
		spinner.enterText(value);
	}

	/**
	 * Sets a mouse automation minimum delay
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationMinDelay(String value, int row,
			FrameFixture preferencesFrame) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesFrame);
	}

	/**
	 * Sets a mouse automation scan rate
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationScanRate(String value, int row,
			FrameFixture preferencesFrame) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_SCAN_RATE,
				preferencesFrame);
	}

	/**
	 * Sets a mouse automation timeout
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesFrame
	 *            The preferencesFrame
	 */
	public static void setAutomationTimeout(String value, int row,
			FrameFixture preferencesFrame) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_TIMEOUT,
				preferencesFrame);
	}

	/**
	 * Opens the next file
	 */
	public static void clickNextFile() {
		getNextButton().click();
	}

	/**
	 * Opens the previous file
	 */
	public static void clickPrevFile() {
		getPrevButton().click();
	}

	/**
	 * Opens the file menu
	 * 
	 * @return The file menu
	 */
	public static JMenuItemFixture openFileMenu() {
		return window.menuItemWithPath(MainFrame.MENU_FILE).click();
	}

	/**
	 * Opens the exit menu
	 */
	public static void openExitMenu() {
		window.menuItemWithPath(MainFrame.MENU_FILE, MainFrame.MENU_ITEM_EXIT)
				.click();
	}

	/**
	 * Deletes an entry
	 * 
	 * @param index
	 *            The index to delete
	 */
	public static void deleteEntry(int index) {

		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_DELETE).click();

	}

	/**
	 * Moves a file list entry one position up
	 * 
	 * @param index
	 *            The index to move one step up
	 */
	public static void moveUpEntry(int index) {

		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_UP).click();
	}

	/**
	 * Moves a file entry one position up
	 * 
	 * @param index
	 *            The index to move one step up
	 */
	public static void moveDownEntry(int index) {

		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_DOWN).click();
	}

	/**
	 * Adds a new file to the entry list
	 * 
	 * @param name
	 *            name of the entry
	 * @param filePath
	 *            path to the file
	 * @param programPath
	 *            path to the opening program
	 */
	public static void addFile(String name, String filePath, String programPath) {

		FrameFixture addFrame = openAddDialog();
		addFrame.textBox(AddFrame.NAME_NAME_TEXT_FIELD).setText(name);
		addFrame.textBox(AddFrame.NAME_FILE_TEXT_FIELD).setText(filePath);
		addFrame.textBox(AddFrame.NAME_PROGRAM_TEXT_FIELD).setText(programPath);
		saveDialog(addFrame);
	}

	/**
	 * Opens the popup menu
	 * 
	 * @return The popup menu
	 */
	public static JPopupMenuFixture openFileListPopupMenu() {

		JListFixture fileList = getFileList();
		return fileList.showPopupMenu();
	}

	/**
	 * Opens the popup menu
	 * 
	 * @param index
	 *            The index of the list entry to open the popup menu on
	 * @return The popup menu
	 */
	public static JPopupMenuFixture openFileListPopupMenu(int index) {

		JListFixture fileList = getFileList();
		return fileList.showPopupMenuAt(index);
	}

	/**
	 * Opens the popup menu on the "next" button.
	 * 
	 * @return The popup menu
	 */
	public static JPopupMenuFixture openNextButtonPopupMenu() {
		return getNextButton().showPopupMenu();
	}

	/**
	 * Opens the popup menu on the "previous" button.
	 * 
	 * @return The popup menu
	 */
	public static JPopupMenuFixture openPrevButtonPopupMenu() {
		return getPrevButton().showPopupMenu();
	}

	/**
	 * Opens the add dialog
	 * 
	 * @return The add dialog as FrameFixtrue
	 */
	public static FrameFixture openAddDialog() {

		focusMidiAutomator();
		JPopupMenuFixture popupMenu = openFileListPopupMenu();
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_ADD).click();
		return WindowFinder.findFrame(AddFrame.NAME).using(robot);
	}

	/**
	 * Opens the edit dialog
	 * 
	 * @param index
	 *            The index of the entry to edit
	 * @return The edit dialog as FrameFixture
	 */
	public static FrameFixture openEditDialog(int index) {

		focusMidiAutomator();
		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_EDIT).click();
		return WindowFinder.findFrame(EditFrame.NAME).using(robot);
	}

	/**
	 * Gets the file list of the main window
	 * 
	 * @return The file list
	 */
	public static JListFixture getFileList() {
		return window.list(MainFrame.NAME_FILE_LIST);
	}

	/**
	 * Gets the midi IN detect label
	 * 
	 * @return The label
	 */
	public static JLabelFixture getMidiINDetect() {
		return window.label(MainFrame.NAME_MIDI_IN_DETECT_LABEL);
	}

	/**
	 * Gets the midi IN detect label
	 * 
	 * @return The label
	 */
	public static JLabelFixture getMidiOUTDetect() {
		return window.label(MainFrame.NAME_MIDI_OUT_DETECT_LABEL);
	}

	/**
	 * Gets the next button
	 * 
	 * @return The next button
	 */
	public static JButtonFixture getNextButton() {
		return window.button(MainFrame.NAME_NEXT_BUTTON);
	}

	/**
	 * Gets the previous button
	 * 
	 * @return The previous button
	 */
	public static JButtonFixture getPrevButton() {
		return window.button(MainFrame.NAME_PREV_BUTTON);
	}

	/**
	 * Gets the info label of the main window
	 * 
	 * @return The info text box
	 */
	public static JTextComponentFixture getInfoLabel() {
		return window.textBox(MainFrame.NAME_INFO_LABEL);
	}

	/**
	 * Gets the string of the info label cleaned up from newlines, tabs and
	 * multiple whitespaces.
	 * 
	 * @return A cleaned string
	 */
	public static String getInfoLabelText() {
		return getInfoLabel().text().replaceAll("\\s+", " ");
	}

	/**
	 * Performs drag and drop from the source location to the target location
	 * 
	 * @param source
	 *            The source location
	 * @param target
	 *            The target location
	 * @param robot
	 *            A robot instance
	 */
	public static void dragElement(Point source, Point target, Robot robot) {

		robot.pressMouse(source, MouseButton.LEFT_BUTTON);
		robot.moveMouse(target);
		robot.releaseMouseButtons();
	}

	/**
	 * Opens an entry of the list.
	 * 
	 * @param index
	 *            The index of the entry
	 */
	public static void openEntryByDoubleClick(int index) {

		getFileList().item(index).doubleClick();
	}

	/**
	 * Sets the focus on the Midi Automator window
	 * 
	 */
	public static void focusMidiAutomator() {
		window.click();
	}

	/**
	 * Opens a search file chooser
	 * 
	 * @param frame
	 *            The frame containing the search button
	 * @param searchButtonName
	 *            The name of the search button
	 * @return The file chooser fixture
	 */
	public static JFileChooserFixture openSearchDialog(FrameFixture frame,
			String searchButtonName) {
		frame.button(searchButtonName).click();
		return JFileChooserFinder.findFileChooser().using(robot);
	}

	/**
	 * Cancels a dialog
	 * 
	 * @param frame
	 *            The frame containing the search button
	 */
	public static void cancelDialog(FrameFixture frame) {
		frame.button("cancel button").click();
	}

	/**
	 * Saves a dialog
	 * 
	 * @param frame
	 *            The frame containing the save button
	 */
	public static void saveDialog(FrameFixture frame) {
		frame.button("save button").click();
	}

	/**
	 * Closes the Midi Automator program
	 */
	public static void closeMidiAutomator() {
		openExitMenu();
	}

	/**
	 * Restarts the MIDI Automator and sets search region to it
	 * 
	 * @throws FindFailed
	 * @throws IOException
	 */
	public static void restartMidiAutomator() throws IOException {

		closeMidiAutomator();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		GUIAutomations.openMidiAutomator();
	}

	/**
	 * Sends the desired key code to the main frame.
	 * 
	 * @param keyCode
	 */
	public static void pressKeyOnMainFrame(int keyCode) {

		window.textBox(MainFrame.NAME_INFO_LABEL).pressKey(keyCode);
	}

}
