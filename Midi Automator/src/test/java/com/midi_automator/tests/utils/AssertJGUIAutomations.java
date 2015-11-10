package com.midi_automator.tests.utils;

import java.io.IOException;

import org.assertj.swing.core.Robot;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Location;

import com.midi_automator.utils.SystemUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.frames.AddFrame;
import com.midi_automator.view.frames.MainFrame;

public class AssertJGUIAutomations {

	public static FrameFixture window;
	public static Robot robot;

	/**
	 * Opens the Midi Automator installer
	 * 
	 * @throws IOException
	 * @throws FindFailed
	 */
	public static void openMidiAutomatorInstaller() throws IOException,
			FindFailed {

		String[] command = null;

		if (System.getProperty("os.name").equals("Mac OS X")) {
			command = new String[] { "open", "-n", "target/MIDI Automator.dmg" };
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			command = new String[] { SystemUtils
					.replaceSystemVariables("\"%MIDIAUTOMATORGIT%\\Midi Automator\\target\\midiautomator_setup.exe\"") };
		}

		SystemUtils.runShellCommand(command);
		focusMidiAutomatorInstaller();
	}

	/**
	 * Closes the Midi Automator installer
	 * 
	 * @throws IOException
	 * @throws FindFailed
	 */
	public static void closeMidiAutomatorInstaller() throws IOException,
			FindFailed {

		String[] command = null;

		if (System.getProperty("os.name").equals("Mac OS X")) {
			command = new String[] { "diskutil", "unmountDisk",
					"/Volumes/MIDI Automator" };
			SystemUtils.runShellCommand(command);
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			try {
				closeNSISInstaller();
			} catch (FindFailed e) {
				cancelNSISInstaller();
			}
		}
	}

	/**
	 * Cancels the NSIS installer
	 * 
	 * @throws FindFailed
	 */
	public static void cancelNSISInstaller() throws FindFailed {
		// Region match = AssertJGUIAutomations.findMultipleStateRegion(
		// DEFAULT_TIMEOUT, "NSIS_cancel_button.png");
		// match.click();
	}

	/**
	 * Closes the NSIS installer
	 * 
	 * @throws FindFailed
	 */
	public static void closeNSISInstaller() throws FindFailed {
		// Region match = AssertJGUIAutomations.findMultipleStateRegion(
		// DEFAULT_TIMEOUT, "NSIS_close_button_active");
		// match.click();
	}

	/**
	 * Finds the region of the Midi Automator installer main Window
	 * 
	 * @return the found region
	 * @throws FindFailed
	 */
	public static void findMidiAutomatorInstallerRegion() {

		// try {
		// setMinSimilarity(LOW_SIMILARITY);
		// SikuliAutomation.setSearchRegion(screen);
		// Region searchRegion = findMultipleStateRegion(MAX_TIMEOUT,
		// "midi_automator_installer.png");
		// setMinSimilarity(DEFAULT_SIMILARITY);
		// searchRegion.y -= 41;
		// searchRegion.h += 41;
		// return searchRegion;
		// } catch (FindFailed e) {
		// System.err.println("findMidiAutomatorInstallerRegion() failed");
		// throw e;
		// }
	}

	/**
	 * Sets the focus on the Midi Automator installer window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomatorInstaller() {
		// try {
		// SikuliAutomation
		// .setSearchRegion(findMidiAutomatorInstallerRegion());
		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT,
		// "Midi_Automator_installer_title.png",
		// "Midi_Automator_installer_title_inactive.png");
		// match.click(match.offset(50, 20));
		// } catch (FindFailed e) {
		// System.err.println("focusMidiAutomatorInstaller() failed");
		// throw e;
		// }
	}

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

		if (System.getProperty("os.name").equals("Windows 7")) {
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
	 * Puts the program to midi learn for the given screenshot
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param similarity
	 *            the minimum similarity
	 * @throws FindFailed
	 */
	public static void midiLearnMainScreen(String state1, String state2,
			String state3, float similarity) {
		// openPopupMenu(state1, state2, state3, similarity);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "midi_learn.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Unlearns a midi message on the given screenshot
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void midiUnlearnMainScreen(String state1, String state2,
			String state3) {
		// openPopupMenu(state1, state2, state3, LOW_SIMILARITY);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "midi_unlearn.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Opens a popup menu on the automations table
	 * 
	 * @param columnScreenshot
	 *            The creenshot of the column header
	 * @param row
	 *            The row number where the popup shall be opened
	 * @throws FindFailed
	 */
	public static void openPopUpMenuAutomation(String columnScreenshot, int row)
			throws FindFailed {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + columnScreenshot, MAX_TIMEOUT);
		// match.rightClick(match.offset(0, row * 30));
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void midiLearnAutomation(int row) {

		// openPopUpMenuAutomation("automation_midi_message.png", row);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "midi_learn.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void cancelMidiLearnAutomation(int row) {

		// openPopUpMenuAutomation("automation_midi_message.png", row);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "cancel_midi_learn.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Cancels the midi learn for the given screenshot
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void cancelMidiLearnMainScreen(String state1, String state2,
			String state3) {
		// openPopupMenu(state1, state2, state3, LOW_SIMILARITY);
		// SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "cancel_midi_learn.png", MAX_TIMEOUT);
		// SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		// match.click();
	}

	/**
	 * Deletes an automation by row number
	 * 
	 * @param row
	 *            The row number to delete
	 * @throws FindFailed
	 */
	public static void deleteAutomation(int row) {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "automation_midi_message.png", MAX_TIMEOUT);
		// match.click(match.offset(0, row * 30));
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "delete_automation_button.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Adds an empty automation
	 * 
	 * @throws FindFailed
	 */
	public static void addAutomation() {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "add_automation_button.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Opens the preferences window.
	 * 
	 * @throws FindFailed
	 */
	public static void openPreferences() {
		// openFileMenu();
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "preferences.png", MAX_TIMEOUT);
		// match.click();
		// SikuliAutomation.setSearchRegion(screen);
		// SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "midi_automator_preferences.png", MAX_TIMEOUT);
		// SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		// match.y -= 20;
		// match.h += 20;
		// SikuliAutomation.setSearchRegion(match);
	}

	/**
	 * Opens the import window.
	 * 
	 * @throws FindFailed
	 */
	public static void openImport() {
		// openFileMenu();
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "import.png", MAX_TIMEOUT);
		// match.click();
		// SikuliAutomation.setSearchRegion(screen);
		// SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "midi_automator_import.png", MAX_TIMEOUT);
		// SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		// match.y -= 20;
		// match.h += 20;
		// SikuliAutomation.setSearchRegion(match);
	}

	/**
	 * Sets the Midi Remote IN device to the given screenshot and saves the
	 * preferences
	 * 
	 * @param scLabel
	 *            The label of the combo box
	 * @param scValue
	 *            The value to choose
	 * @throws FindFailed
	 */
	public static void setAndSavePreferencesComboBox(String scLabel,
			String scValue) {

		// setPreferencesComboBox(scLabel, scValue);
		// AssertJGUIAutomations.saveDialog();
		// SikuliAutomation.setSearchRegion(AssertJGUIAutomations
		// .findMidiAutomatorRegion());
	}

	/**
	 * Sets the Midi Remote IN device to the given screenshot
	 * 
	 * @param scLabel
	 *            The label of the combo box
	 * @param scValue
	 *            The value to choose
	 * @throws FindFailed
	 */
	public static void setPreferencesComboBox(String scLabel, String scValue)
			throws FindFailed {

		// AssertJGUIAutomations.openPreferences();
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + scLabel, MAX_TIMEOUT);
		// match.click(match.offset(0, 20));
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + scValue, MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Sets and saves a mouse automation option
	 * 
	 * @param scLabel
	 *            The label of the combo box
	 * @param scValue
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void setAndSaveAutomationsComboBox(String scLabel,
			String scValue, int row) {
		// AssertJGUIAutomations.openPreferences();
		// setAutomationsComboBox(scLabel, scValue, row);
		// AssertJGUIAutomations.saveDialog();
		// SikuliAutomation.setSearchRegion(AssertJGUIAutomations
		// .findMidiAutomatorRegion());
	}

	/**
	 * Sets a mouse automation combo box option
	 * 
	 * @param scLabel
	 *            The label of the combo box
	 * @param scValue
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void setAutomationsComboBox(String scLabel, String scValue,
			int row) {

		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + scLabel, MAX_TIMEOUT);
		// match.click(match.offset(0, row * 30));
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + scValue, MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Spins up a spinner
	 * 
	 * @throws FindFailed
	 */
	public static void spinUp() {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "spinner_up.png", MAX_TIMEOUT);
		// match.click();
		// match.mouseMove(match.offset(10, 0));
	}

	/**
	 * Spins down a spinner
	 * 
	 * @throws FindFailed
	 */
	public static void spinDown() {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "spinner_down.png", MAX_TIMEOUT);
		// match.click();
		// match.mouseMove(match.offset(15, 10));
	}

	/**
	 * Clicks a checkbox in the first automation
	 * 
	 * @param automationHeaderImage
	 *            The automation column header
	 * @throws FindFailed
	 */
	public static void clickAutomationCheckBox(String automationHeaderImage)
			throws FindFailed {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + automationHeaderImage, MAX_TIMEOUT);
		// match.click(match.offset(0, 20));
	}

	/**
	 * Activates a mouse automation text field option for setting values
	 * 
	 * @param scLabel
	 *            The screenshot of the label of the combo box
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void activateAutomationsTextField(String scLabel, int row)
			throws FindFailed {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + scLabel, MAX_TIMEOUT);
		// match.doubleClick(match.offset(0, row * 30));
	}

	/**
	 * Sets a mouse automation text field option
	 * 
	 * @param scLabel
	 *            The screenshot of the label of the combo box
	 * @param value
	 *            The value to choose
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void setAutomationsTextField(String scLabel, String value,
			int row) {

		// activateAutomationsTextField(scLabel, row);
		//
		// if (System.getProperty("os.name").equals("Mac OS X")) {
		// screen.type("a", KeyModifier.CMD);
		// }
		//
		// if (System.getProperty("os.name").equals("Windows 7")) {
		// screen.type("a", KeyModifier.CTRL);
		// }
		// screen.paste(value);
	}

	/**
	 * Opens the next file
	 * 
	 * @throws FindFailed
	 */
	public static void nextFile() {
		// SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "next.png", MAX_TIMEOUT);
		// SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		// match.click();
	}

	/**
	 * Opens the previous file
	 * 
	 * @throws FindFailed
	 */
	public static void prevFile() {
		// SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "prev.png", MAX_TIMEOUT);
		// SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		// match.click();
	}

	/**
	 * Opens the file menu
	 * 
	 * @throws FindFailed
	 */
	public static void openFileMenu() {
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "file.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Opens the exit menu
	 * 
	 * @throws FindFailed
	 */
	public static void openExitMenu() {
		// openFileMenu();
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "exit.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Deletes an entry
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param screenshot
	 * @throws FindFailed
	 */
	public static void deleteEntry(String state1, String state2, String state3)
			throws FindFailed {
		// AssertJGUIAutomations.openPopupMenu(state1, state2, state3);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "delete.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Moves a file entry one position up
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void moveUpEntry(String state1, String state2, String state3)
			throws FindFailed {

		// Region match = AssertJGUIAutomations.findMultipleStateRegion(
		// DEFAULT_TIMEOUT, state1, state2, state3);
		//
		// match.rightClick();
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "move_up.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Moves a file entry one position up
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void moveDownEntry(String state1, String state2, String state3)
			throws FindFailed {

		// Region match = AssertJGUIAutomations.findMultipleStateRegion(
		// DEFAULT_TIMEOUT, state1, state2, state3);
		//
		// match.rightClick();
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "move_down.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Adds a new file to the entry list
	 * 
	 * @param name
	 *            name of the entry
	 * @param path
	 *            path to the file
	 */
	public static void addFile(String name, String path) {

		FrameFixture addFrame = openAddDialog();
		addFrame.textBox(AddFrame.NAME_NAME_TEXT_FIELD).setText(name);
		addFrame.textBox(AddFrame.NAME_FILE_TEXT_FIELD).setText(path);
		// saveDialog();
	}

	/**
	 * Opens the popup menu
	 * 
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void openPopupMenu(String state1, String state2, String state3)
			throws FindFailed {
		// openPopupMenu(state1, state2, state3, DEFAULT_SIMILARITY);
	}

	/**
	 * Opens the popup menu
	 * 
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param similarity
	 *            The minimum similarity
	 * @throws FindFailed
	 */
	public static void openPopupMenu(String state1, String state2,
			String state3, float similarity) {
		// setMinSimilarity(similarity);
		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT, state1,
		// state2,
		// state3);
		// setMinSimilarity(DEFAULT_SIMILARITY);
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// match.rightClick();
	}

	/**
	 * Opens the add dialog
	 * 
	 * @return The add dialog as FrameFixtrue
	 */
	public static FrameFixture openAddDialog() {

		JPopupMenuFixture popupMenu = window.list(MainFrame.NAME_FILE_LIST)
				.showPopupMenu();
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_ADD).click();
		return WindowFinder.findFrame(AddFrame.NAME).using(robot);

	}

	/**
	 * Drags an element to the given location.
	 * 
	 * @param location
	 *            The location to drag to
	 * @param state
	 *            The different states of the element (focused, unfocused,
	 *            etc...)
	 * @throws FindFailed
	 */
	public static void dragElement(Location location, String... states)
			throws FindFailed {

		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT, states);
		// match.hover(match);
		// match.mouseDown(Mouse.LEFT);
		// match.drag(location);
		// match.mouseUp();
	}

	/**
	 * Opens the edit dialog
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void openEditDialog(String state1, String state2,
			String state3) {

		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT, state1,
		// state2,
		// state3);
		// match.click();
		//
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// match.rightClick();
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "edit.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Finds a region that can have multiple states, i.e. active, inactive,
	 * unfocused
	 * 
	 * @param timeout
	 *            the timeout to search for every state
	 * @param states
	 *            the different states of the region
	 * @return The found region
	 * @throws FindFailed
	 */
	public static void findMultipleStateRegion(double timeout, String... states) {

		// Region match;
		// FindFailed findFailed = null;
		//
		// for (String state : states) {
		// if (state != null) {
		// try {
		// match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + state, timeout);
		// return match;
		// } catch (FindFailed e) {
		// findFailed = e;
		// System.out.println(state
		// + " not found. Trying next state...");
		// }
		// }
		// }
		//
		// throw findFailed;
	}

	/**
	 * Opens an entry of the list.
	 * 
	 * @param state1
	 *            first try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state2
	 *            second try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @param state3
	 *            third try screenshot (unchoosen, choosen, choosen-unfocused)
	 * @throws FindFailed
	 */
	public static void openEntryByDoubleClick(String state1, String state2,
			String state3) {

		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT, state1,
		// state2,
		// state3);
		// match.doubleClick();
	}

	/**
	 * Sets the focus on the Midi Automator window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomator() {
		// try {
		// SikuliAutomation.setSearchRegion(findMidiAutomatorRegion());
		// Region match = findMultipleStateRegion(DEFAULT_TIMEOUT,
		// "Midi_Automator_title.png",
		// "Midi_Automator_title_inactive.png");
		// match.click(match.offset(50, 20));
		// } catch (FindFailed e) {
		// System.err.println("focusMidiAutomator() failed");
		// throw e;
		// }
	}

	/**
	 * Finds the region of the Midi Automator main Window
	 * 
	 * @return the found region
	 * @throws FindFailed
	 */
	public static void findMidiAutomatorRegion() {

		// try {
		// setMinSimilarity(LOW_SIMILARITY);
		// SikuliAutomation.setSearchRegion(screen);
		// Region searchRegion = findMultipleStateRegion(MAX_TIMEOUT,
		// "midi_automator.png");
		// setMinSimilarity(DEFAULT_SIMILARITY);
		// searchRegion.y = searchRegion.y - 21;
		// searchRegion.w = searchRegion.w + 500;
		// searchRegion.h = searchRegion.h + 100;
		// return searchRegion;
		// } catch (FindFailed e) {
		// System.err.println("findMidiAutomatorRegion() failed");
		// throw e;
		// }
	}

	/**
	 * Fails the test with a given error message if a screenshot was found
	 * 
	 * @param screenshot
	 *            The screenshot to search for
	 * @param error
	 *            The error text
	 */
	public static void checkIfNoResult(String screenshot, String error) {
		// try {
		// AssertJGUIAutomations.checkResult(screenshot, DEFAULT_TIMEOUT);
		// fail(error);
		// } catch (FindFailed e) {
		//
		// } finally {
		// setMinSimilarity(DEFAULT_SIMILARITY);
		// }
	}

	/**
	 * Checks for the result screenshot
	 * 
	 * @param resultScreenshot
	 *            The screenshot that should be shown
	 * @throws FindFailed
	 */
	public static void checkResult(String resultScreenshot) {
		// checkResult(resultScreenshot, HIGH_SIMILARITY, MAX_TIMEOUT);
	}

	/**
	 * Checks for the result screenshot
	 * 
	 * @param resultScreenshot
	 *            The screenshot that should be shown
	 * @param timeout
	 *            The timeout to search for the result screenshot
	 * @throws FindFailed
	 */
	public static void checkResult(String resultScreenshot, double timeout)
			throws FindFailed {
		// checkResult(resultScreenshot, HIGH_SIMILARITY, timeout);
	}

	/**
	 * Checks for the result screenshot
	 * 
	 * @param resultScreenshot
	 *            The screenshot that should be shown
	 * @param similarity
	 *            The min similarity
	 * @throws FindFailed
	 */
	public static void checkResult(String resultScreenshot, float similarity)
			throws FindFailed {
		// checkResult(resultScreenshot, similarity, MAX_TIMEOUT);
	}

	/**
	 * Checks for the result screenshot
	 * 
	 * @param resultScreenshot
	 *            The screenshot that should be shown
	 * @param similarity
	 *            The min similarity
	 * @param timeout
	 *            The timeout to search for the result screenshot
	 * @throws FindFailed
	 */
	public static void checkResult(String resultScreenshot, float similarity,
			double timeout) {
		// setMinSimilarity(similarity);
		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + resultScreenshot, timeout);
		// setMinSimilarity(DEFAULT_SIMILARITY);
		// match.highlight(HIGHLIGHT_DURATION);
	}

	/**
	 * Checks if the file opened correctly
	 * 
	 * @param active
	 *            screenshot of active window
	 * @param inactive
	 *            screenshot of inactive window
	 * @return <TRUE> if file was opened, <FALSE> if not
	 * @throws FindFailed
	 */
	public static void checkIfFileOpened(String active, String inactive)
			throws FindFailed {

		// Region match = null;
		//
		// try {
		//
		// // check if file opened
		// SikuliAutomation.setSearchRegion(screen);
		// match = findMultipleStateRegion(DEFAULT_TIMEOUT, active, inactive);
		// match.highlight(HIGHLIGHT_DURATION);
		//
		// // close editor
		// match.click();
		// closeFocusedProgram();
		//
		// } catch (FindFailed e) {
		// throw e;
		// } finally {
		//
		// try {
		// focusMidiAutomator();
		// } catch (FindFailed e) {
		// e.printStackTrace();
		// }
		// }
	}

	/**
	 * Minimizes the current focused program
	 */
	public static void hideFocusedProgram() {
		// if (System.getProperty("os.name").equals("Mac OS X")) {
		// screen.type("h", KeyModifier.CMD);
		// }
		// if (System.getProperty("os.name").equals("Windows 7")) {
		// screen.type(Key.DOWN, KeyModifier.WIN);
		// }
	}

	/**
	 * Closes the current focused program.
	 */
	public static void closeFocusedProgram() {
		// if (System.getProperty("os.name").equals("Mac OS X")) {
		// screen.type("q", Key.CMD);
		// }
		// if (System.getProperty("os.name").equals("Windows 7")) {
		// screen.type(Key.F4, KeyModifier.WIN | KeyModifier.ALT);
		// }
	}

	/**
	 * Shows the Midi Automator main window when it was hidden.
	 * 
	 * @throws FindFailed
	 */
	public static void unhideMidiAutomator() {
		// show Midi Automator
		// if (System.getProperty("os.name").equals("Mac OS X")) {
		// screen.type(Key.TAB, Key.CMD);
		//
		// // MAc Dock not recognized by Sikuli
		// // Region match = SCREEN.wait(screenshotpath
		// // + "midi_automator_icon.png", TIMEOUT);
		// // match.click();
		// }
		// if (System.getProperty("os.name").equals("Windows 7")) {
		// screen.type(Key.TAB, Key.ALT);
		// screen.type(Key.TAB, Key.ALT);
		// }
	}

	/**
	 * Opens a search file chooser
	 * 
	 * @throws FindFailed
	 */
	public static void openSearchDialog() {

		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "search_button.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Cancels a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void cancelDialog() {

		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "cancel_button.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Saves a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void saveDialog() {

		// Region match = SikuliAutomation.getSearchRegion().wait(
		// screenshotpath + "save_button.png", MAX_TIMEOUT);
		// match.click();
	}

	/**
	 * Closes the Midi Automator program
	 * 
	 * @throws FindFailed
	 */
	public static void closeMidiAutomator() {

		focusMidiAutomator();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		closeFocusedProgram();
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
		AssertJGUIAutomations.openMidiAutomator();
	}

}
