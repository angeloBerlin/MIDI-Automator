package com.midi_automator.tests.utils;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JSpinner;

import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.core.MouseButton;
import org.assertj.swing.core.Robot;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.finder.JFileChooserFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.AbstractWindowFixture;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JCheckBoxFixture;
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
import com.midi_automator.view.KeyLearnPopupMenu;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.MidiLearnPopupMenu;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationPanel;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;
import com.midi_automator.view.automationconfiguration.ImagePopupMenu;
import com.midi_automator.view.frames.AddDialog;
import com.midi_automator.view.frames.EditDialog;
import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.frames.PreferencesDialog;

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
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(MidiLearnPopupMenu.NAME_MENU_ITEM_MIDI_UNLEARN)
				.click();
	}

	/**
	 * Cancels midi learning for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void cancelMidiLearnAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIDI_SIGNATURE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItemWithPath(
				MidiLearnPopupMenu.MENU_ITEM_MIDI_LEARN_CANCEL).click();
	}

	/**
	 * Learns a key for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void keyLearnAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(KeyLearnPopupMenu.NAME_MENU_ITEM_KEYS_LEARN).click();
	}

	/**
	 * Submits key learning for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void submitKeyLearnAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(KeyLearnPopupMenu.NAME_MENU_ITEM_KEYS_SUBMIT)
				.click();
	}

	/**
	 * Un-learns a key for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void keyUnLearnAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(KeyLearnPopupMenu.NAME_MENU_ITEM_KEYS_UNLEARN)
				.click();
	}

	/**
	 * Cancels key learning for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void cancelKeyLearnAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(KeyLearnPopupMenu.NAME_MENU_ITEM_KEYS_CANCEL)
				.click();
	}

	/**
	 * Removes a screenshot from an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @param prferencesFrame
	 *            The preferences frame
	 */
	public static void removeScreenshotFromAutomation(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_IMAGE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(ImagePopupMenu.NAME_MENU_ITEM_REMOVE_IMAGE).click();
	}

	/**
	 * Deletes an automation by row number
	 * 
	 * @param row
	 *            The row number to delete
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void deleteAutomation(int row, DialogFixture preferencesDialog) {
		getGUIAutomationTable(preferencesDialog).selectRows(row);
		preferencesDialog.button(
				GUIAutomationConfigurationPanel.NAME_DELETE_BUTTON).click();
	}

	/**
	 * Deletes all automations
	 * 
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void deleteAllAutomations(DialogFixture preferencesDialog) {

		int rowCount = getGUIAutomationTable(preferencesDialog).rowCount();

		for (int i = 0; i < rowCount; i++) {
			deleteAutomation(0, preferencesDialog);
		}
	}

	/**
	 * Deletes all active automations
	 */
	public static void resetAutomations() {
		DialogFixture preferencesDialog = openPreferences();
		deleteAllAutomations(preferencesDialog);
		saveDialog(preferencesDialog);
	}

	/**
	 * Adds an empty automation
	 * 
	 * @param preferencesDialog
	 *            The preferences Frame
	 */
	public static void addAutomation(DialogFixture preferencesDialog) {
		preferencesDialog.button(
				GUIAutomationConfigurationPanel.NAME_ADD_BUTTON).click();
	}

	/**
	 * Opens the preferences window.
	 * 
	 * @return The preferences window
	 */
	public static DialogFixture openPreferences() {

		window.menuItemWithPath(MainFrame.MENU_FILE,
				MainFrame.MENU_ITEM_PREFERENCES).click();

		return WindowFinder.findDialog(PreferencesDialog.NAME).using(robot);
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
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setMidiInRemoteDevice(String value,
			DialogFixture preferencesDialog) {
		setPreferencesComboBox(
				PreferencesDialog.NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX, value,
				preferencesDialog);
	}

	/**
	 * Sets the MIDI OUT list entry device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setMidiOutSwitchItemDevice(String value,
			DialogFixture preferencesDialog) {
		setPreferencesComboBox(
				PreferencesDialog.NAME_MIDI_OUT_SWITCH_ITEM_DEVICE_COMBO_BOX,
				value, preferencesDialog);
	}

	/**
	 * Sets the MIDI IN metronom device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setMidiInMetronomDevice(String value,
			DialogFixture preferencesDialog) {
		setPreferencesComboBox(
				PreferencesDialog.NAME_MIDI_IN_METRONROM_DEVICE_COMBO_BOX,
				value, preferencesDialog);
	}

	/**
	 * Sets the MIDI OUT notifier device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setMidiOutNotifierDevice(String value,
			DialogFixture preferencesDialog) {
		setPreferencesComboBox(
				PreferencesDialog.NAME_MIDI_OUT_SWITCH_NOTIFIER_DEVICE_COMBO_BOX,
				value, preferencesDialog);
	}

	/**
	 * Sets the MIDI OUT remote device
	 * 
	 * @param value
	 *            The value of the combo box
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setMidiOutRemoteDevice(String value,
			DialogFixture preferencesDialog) {
		setPreferencesComboBox(
				PreferencesDialog.NAME_MIDI_OUT_REMOTE_DEVICE_COMBO_BOX, value,
				preferencesDialog);
	}

	/**
	 * Clicks the switch notifier send button
	 * 
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void clickNotifierSendButton(DialogFixture preferencesDialog) {
		preferencesDialog.button(PreferencesDialog.NAME_BUTTON_SEND_NOTIFIER)
				.click();
	}

	/**
	 * Sets the preferences combo box to the given value
	 * 
	 * @param name
	 *            The name of the combo box
	 * @param value
	 *            The value of the combo box
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void setPreferencesComboBox(String name, String value,
			DialogFixture preferencesDialog) {
		preferencesDialog.comboBox(name).selectItem(value);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationsComboBox(String value, int row,
			int column, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationsComboBox(String value, int row,
			String columnName, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		table.enterValue(
				TableCell.row(row).column(table.columnIndexFor(columnName)),
				value);
	}

	/**
	 * Gets an automation combobox
	 * 
	 * @param row
	 *            The row of the automation
	 * @param columnName
	 *            The name of the column
	 * @param preferencesDialog
	 *            The preferences dialog
	 * @return The combo box of the automation
	 * 
	 */
	public static JComboBox<?> getAutomationsComboBox(int row,
			String columnName, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(
				table.columnIndexFor(columnName)));
		Component editor = cell.editor();

		if (editor instanceof JComboBox) {
			return (JComboBox<?>) editor;
		}

		return null;
	}

	/**
	 * Gets the focus program combo box
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferences dialog
	 * @return The combo box for the program focus
	 */
	public static JComboBox<?> getFocusProgramComboBox(int row,
			DialogFixture preferencesDialog) {
		JComboBox<?> comboBox = getAutomationsComboBox(0,
				GUIAutomationConfigurationTable.COLNAME_FOCUS,
				preferencesDialog);

		return comboBox;
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationTrigger(String value, int row,
			DialogFixture preferencesDialog) {

		setAutomationsComboBox(value, row,
				GUIAutomationConfigurationTable.COLNAME_TRIGGER,
				preferencesDialog);
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationType(String value, int row,
			DialogFixture preferencesDialog) {
		setAutomationsComboBox(value, row,
				GUIAutomationConfigurationTable.COLNAME_TYPE, preferencesDialog);
	}

	/**
	 * Sets a focus program combo box option to the last item found for the
	 * value.
	 * 
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setFocusedProgram(String value, int row,
			DialogFixture preferencesDialog) {

		JComboBox<?> comboBox = getFocusProgramComboBox(row, preferencesDialog);

		for (int i = 0; i < comboBox.getItemCount(); i++) {
			String item = (String) comboBox.getItemAt(i);

			if (item.contains(value)) {
				setAutomationsComboBox(item, row,
						GUIAutomationConfigurationTable.COLNAME_FOCUS,
						preferencesDialog);
			}
		}
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void spinUpAutomationDelaySpinner(int times, int row,
			DialogFixture preferencesDialog) {

		spinUpAutomationsSpinner(times, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void spinDownAutomationDelaySpinner(int times, int row,
			DialogFixture preferencesDialog) {

		spinDownAutomationsSpinner(times, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesDialog);
	}

	/**
	 * Gets the coordinates of the table cell of the automations min delay
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferences frame
	 * @return The TableCell
	 */
	public static TableCell automationsDelayCell(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void spinUpAutomationsSpinner(int times, int row, int column,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.doubleClick();

		JSpinnerFixture spinnerEditor = getAutomationsSpinner(row, column,
				preferencesDialog);

		spinnerEditor.increment(times);
		spinnerEditor.pressAndReleaseKey(KeyPressInfo
				.keyCode(KeyEvent.VK_ENTER));
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void spinUpAutomationsSpinner(int times, int row,
			String columnName, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		spinUpAutomationsSpinner(times, row, table.columnIndexFor(columnName),
				preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferences frame
	 */
	public static void spinDownAutomationsSpinner(int times, int row,
			int column, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.doubleClick();

		JSpinnerFixture spinnerEditor = getAutomationsSpinner(row, column,
				preferencesDialog);

		spinnerEditor.decrement(times);
		spinnerEditor.pressAndReleaseKey(KeyPressInfo
				.keyCode(KeyEvent.VK_ENTER));
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void spinDownAutomationsSpinner(int times, int row,
			String columnName, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		spinDownAutomationsSpinner(times, row,
				table.columnIndexFor(columnName), preferencesDialog);
	}

	/**
	 * Gets a Spinner from the automation table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesDialog
	 *            The preferences frame
	 * @return
	 */
	public static JSpinnerFixture getAutomationsSpinner(int row, int column,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferences frame
	 * @return
	 */
	public static JSpinnerFixture getAutomationsDelaySpinner(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);

		int index = table
				.columnIndexFor(table
						.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MIN_DELAY));

		return getAutomationsSpinner(row, index, preferencesDialog);
	}

	/**
	 * Clicks a check box in the automations table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param column
	 *            The column of the automation
	 * @param preferencesDialog
	 *            The preferences Frame
	 */
	public static void clickAutomationCheckBox(int row, int column,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		JTableCellFixture cell = table.cell(TableCell.row(row).column(column));
		cell.click();
	}

	/**
	 * Clicks a check box in the automations table
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferences Frame
	 */
	public static void clickAutomationMovableCheckBox(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_MOVABLE);
		clickAutomationCheckBox(row, column, preferencesDialog);
	}

	/**
	 * Gets the automation table from the preferences frame
	 * 
	 * @param preferencesDialog
	 *            The preferences frame
	 * @return The GUI automation table
	 */
	public static JTableFixture getGUIAutomationTable(
			DialogFixture preferencesDialog) {
		return preferencesDialog.table(GUIAutomationConfigurationTable.NAME);
	}

	/**
	 * Opens the screen shot file chooser
	 * 
	 * @param row
	 *            The row of the automation
	 * @param preferencesDialog
	 *            The preferencesDialog
	 * @return The file chooser fixture
	 */
	public static JFileChooserFixture openScreenshotFileChooser(int row,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_IMAGE);

		JPopupMenuFixture popupMenu = table.showPopupMenuAt(TableCell.row(row)
				.column(column));
		popupMenu.menuItem(ImagePopupMenu.NAME_MENU_ITEM_NEW_IMAGE).click();
		return JFileChooserFinder.findFileChooser().using(robot);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationsSpinner(String value, int row, int column,
			DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationsSpinner(String value, int row,
			String columnName, DialogFixture preferencesDialog) {

		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		setAutomationsSpinner(value, row, table.columnIndexFor(columnName),
				preferencesDialog);
	}

	/**
	 * Sets a mouse automation minimum similarity
	 * 
	 * @param value
	 *            The value to choose
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationMinSimilarity(String value,
			DialogFixture preferencesDialog) {

		JSpinnerFixture spinner = preferencesDialog
				.spinner(GUIAutomationConfigurationPanel.NAME_MIN_SIMILARITY_SPINNER);
		spinner.enterTextAndCommit(value);
	}

	/**
	 * Checks the checkbox for minimizing the program frame on close.
	 * 
	 * @param checked
	 *            <TRUE> check the checkbox, <FALSE> uncheck the checkbox
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void checkMinimizeOnClose(boolean checked,
			DialogFixture preferencesDialog) {

		JCheckBoxFixture checkbox = preferencesDialog
				.checkBox(PreferencesDialog.NAME_CHECKBOX_MINIMIZE_ON_CLOSE);
		checkbox.check(checked);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationMinDelay(String value, int row,
			DialogFixture preferencesDialog) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_MIN_DELAY,
				preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationScanRate(String value, int row,
			DialogFixture preferencesDialog) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_SCAN_RATE,
				preferencesDialog);
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
	 * @param preferencesDialog
	 *            The preferencesDialog
	 */
	public static void setAutomationTimeout(String value, int row,
			DialogFixture preferencesDialog) {

		setAutomationsSpinner(value, row,
				GUIAutomationConfigurationTable.COLNAME_TIMEOUT,
				preferencesDialog);
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
	 * @return the add frame
	 */
	public static DialogFixture addFile(String name, String filePath,
			String programPath) {

		DialogFixture addDialog = openAddDialog();
		addDialog.textBox(AddDialog.NAME_NAME_TEXT_FIELD).setText(name);
		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).setText(filePath);
		addDialog.textBox(AddDialog.NAME_PROGRAM_TEXT_FIELD).setText(
				programPath);
		return addDialog;
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
	public static DialogFixture openAddDialog() {

		focusMidiAutomator();
		JPopupMenuFixture popupMenu = openFileListPopupMenu();
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_ADD).click();
		return WindowFinder.findDialog(AddDialog.NAME).using(robot);
	}

	/**
	 * Opens the edit dialog
	 * 
	 * @param index
	 *            The index of the entry to edit
	 * @return The edit dialog as FrameFixture
	 */
	public static DialogFixture openEditDialog(int index) {

		focusMidiAutomator();
		JPopupMenuFixture popupMenu = openFileListPopupMenu(index);
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_EDIT).click();
		return WindowFinder.findDialog(EditDialog.NAME).using(robot);
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
	 * Selects an entry of the list.
	 * 
	 * @param index
	 *            The index of the entry
	 */
	public static void selectEntryByLeftClick(int index) {

		getFileList().item(index).click();
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
	 * @param window
	 *            The window containing the search button
	 * @param searchButtonName
	 *            The name of the search button
	 * @return The file chooser fixture
	 */
	public static JFileChooserFixture openSearchDialog(
			AbstractWindowFixture<?, ?, ?> window, String searchButtonName) {
		window.button(searchButtonName).click();
		return JFileChooserFinder.findFileChooser().using(robot);
	}

	/**
	 * Opens a search file chooser
	 * 
	 * @param window
	 *            The window containing the search button
	 * @param searchButtonName
	 *            The name of the search button
	 * @return The file chooser fixture
	 */
	public static JFileChooserFixture openSearchDialogOnEnter(
			AbstractWindowFixture<?, ?, ?> window, String searchButtonName) {
		window.button(searchButtonName).pressAndReleaseKeys(KeyEvent.VK_ENTER);
		return JFileChooserFinder.findFileChooser().using(robot);
	}

	/**
	 * Cancels a dialog
	 * 
	 * @param dialog
	 *            The dialog containing the search button
	 */
	public static void cancelDialog(DialogFixture dialog) {
		dialog.button("cancel button").click();
	}

	/**
	 * Cancels a dialog by hitting enter on the save button
	 * 
	 * @param window
	 *            The window containing the save button
	 */
	public static void cancelDialogByEnter(AbstractWindowFixture<?, ?, ?> window) {
		window.button("cancel button").pressAndReleaseKeys(KeyEvent.VK_ENTER);
	}

	/**
	 * Saves a dialog by clicking the save button
	 * 
	 * @param dialog
	 *            The dialog containing the save button
	 */
	public static void saveDialog(DialogFixture dialog) {
		dialog.button("save button").click();
	}

	/**
	 * Saves a dialog by hitting enter on the save button
	 * 
	 * @param dialog
	 *            The dialog containing the save button
	 */
	public static void saveDialogByEnter(DialogFixture dialog) {
		dialog.button("save button").pressAndReleaseKeys(KeyEvent.VK_ENTER);
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
	 * Presses the desired key code to the main frame.
	 * 
	 * @param keyCode
	 *            The key code pressed
	 */
	public static void pressKeyOnMainFrame(int keyCode) {

		window.textBox(MainFrame.NAME_INFO_LABEL).pressKey(keyCode);
	}

	/**
	 * Presses and releases the desired key code to the main frame.
	 * 
	 * @param keyCode
	 *            The key codes pressed and released
	 */
	public static void pressAndReleaseKeysOnMainFrame(int... keyCode) {

		window.textBox(MainFrame.NAME_INFO_LABEL).pressAndReleaseKeys(keyCode);
	}

	/**
	 * Presses the desired key code on the GUI automation table.
	 * 
	 * @param preferencesDialog
	 *            The preferences dialog with the automation table
	 * @param keyCode
	 *            The key code pressed
	 */
	public static void pressKeyOnGUIAutomationTable(
			DialogFixture preferencesDialog, int keyCode) {

		getGUIAutomationTable(preferencesDialog).pressKey(keyCode);
	}

	/**
	 * Presses and releases the desired key code on the GUI automation table.
	 * 
	 * @param preferencesDialog
	 *            The preferences dialog with the automation table
	 * @param keyCode
	 *            The key codes pressed and released
	 */
	public static void pressAndReleaseKeysOnGUIAutomationTable(
			DialogFixture preferencesDialog, int... keyCode) {

		getGUIAutomationTable(preferencesDialog).pressAndReleaseKeys(keyCode);
	}

}
