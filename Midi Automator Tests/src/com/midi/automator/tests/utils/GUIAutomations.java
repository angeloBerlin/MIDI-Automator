package com.midi.automator.tests.utils;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;

public class GUIAutomations extends SikuliAutomation {

	/**
	 * Opens the Midi Automator program
	 * 
	 * @throws IOException
	 * @throws FindFailed
	 */
	public static void openMidiAutomator() throws IOException, FindFailed {

		String[] command = null;

		if (System.getProperty("os.name").equals("Mac OS X")) {
			command = new String[] { "open", "-n",
					"/Applications/Midi Automator.app" };
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			command = new String[] {
					SystemUtils
							.replaceSystemVariables("%ProgramFiles%\\Midi Automator\\Midi Automator.exe"),
					"-test" };
		}

		Runtime.getRuntime().exec(command);
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
			String state3, float similarity) throws FindFailed {
		openPopupMenu(state1, state2, state3, similarity);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_learn.png", MAX_TIMEOUT);
		match.click();
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
			String state3) throws FindFailed {
		openPopupMenu(state1, state2, state3, LOW_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_unlearn.png", MAX_TIMEOUT);
		match.click();
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
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + columnScreenshot, MAX_TIMEOUT);
		match.rightClick(match.offset(0, row * 30));
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void midiLearnAutomation(int row) throws FindFailed {

		openPopUpMenuAutomation("automation_midi_message.png", row);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_learn.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Learns a midi message for an automation for the specified row
	 * 
	 * @param row
	 *            The row of the automation
	 * @throws FindFailed
	 */
	public static void cancelMidiLearnAutomation(int row) throws FindFailed {

		openPopUpMenuAutomation("automation_midi_message.png", row);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "cancel_midi_learn.png", MAX_TIMEOUT);
		match.click();
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
			String state3) throws FindFailed {
		openPopupMenu(state1, state2, state3, LOW_SIMILARITY);
		SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "cancel_midi_learn.png", MAX_TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.click();
	}

	/**
	 * Deletes an automation by row number
	 * 
	 * @param row
	 *            The row number to delete
	 * @throws FindFailed
	 */
	public static void deleteAutomation(int row) throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "automation_midi_message.png", MAX_TIMEOUT);
		match.click(match.offset(0, row * 30));
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "delete_automation_button.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Adds an empty automation
	 * 
	 * @throws FindFailed
	 */
	public static void addAutomation() throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "add_automation_button.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Opens the preferences window.
	 * 
	 * @throws FindFailed
	 */
	public static void openPreferences() throws FindFailed {
		openFileMenu();
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "preferences.png", MAX_TIMEOUT);
		match.click();
		SikuliAutomation.setSearchRegion(SCREEN);
		SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_automator_preferences.png", MAX_TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.y -= 20;
		match.h += 20;
		SikuliAutomation.setSearchRegion(match);
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
			String scValue) throws FindFailed {

		setPreferencesComboBox(scLabel, scValue);
		GUIAutomations.saveDialog();
		SikuliAutomation.setSearchRegion(GUIAutomations
				.findMidiAutomatorRegion());
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

		GUIAutomations.openPreferences();
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scLabel, MAX_TIMEOUT);
		match.click(match.offset(0, 20));
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scValue, MAX_TIMEOUT);
		match.click();
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
			String scValue, int row) throws FindFailed {
		GUIAutomations.openPreferences();
		setAutomationsComboBox(scLabel, scValue, row);
		GUIAutomations.saveDialog();
		SikuliAutomation.setSearchRegion(GUIAutomations
				.findMidiAutomatorRegion());
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
			int row) throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scLabel, MAX_TIMEOUT);
		match.click(match.offset(0, row * 30));
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scValue, MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Spins up a spinner
	 * 
	 * @throws FindFailed
	 */
	public static void spinUp() throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "spinner_up.png", MAX_TIMEOUT);
		match.click();
		match.mouseMove(match.offset(10, 0));
	}

	/**
	 * Spins down a spinner
	 * 
	 * @throws FindFailed
	 */
	public static void spinDown() throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "spinner_down.png", MAX_TIMEOUT);
		match.click();
		match.mouseMove(match.offset(10, 0));
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
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scLabel, MAX_TIMEOUT);
		match.doubleClick(match.offset(0, row * 30));
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
			int row) throws FindFailed {

		activateAutomationsTextField(scLabel, row);

		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("a", KeyModifier.CMD);
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type("a", KeyModifier.CTRL);
		}
		SCREEN.paste(value);
	}

	/**
	 * Opens the next file
	 * 
	 * @throws FindFailed
	 */
	public static void nextFile() throws FindFailed {
		SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "next.png", MAX_TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.click();
	}

	/**
	 * Opens the previous file
	 * 
	 * @throws FindFailed
	 */
	public static void prevFile() throws FindFailed {
		SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "prev.png", MAX_TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.click();
	}

	/**
	 * Opens the file menu
	 * 
	 * @throws FindFailed
	 */
	public static void openFileMenu() throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "file.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Opens the exit menu
	 * 
	 * @throws FindFailed
	 */
	public static void openExitMenu() throws FindFailed {
		openFileMenu();
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "exit.png", MAX_TIMEOUT);
		match.click();
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
		Region match = findMultipleStateRegion(MIN_TIMEOUT, state1, state2,
				state3);
		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "delete.png", MAX_TIMEOUT);
		match.click();
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

		Region match = GUIAutomations.findMultipleStateRegion(MIN_TIMEOUT,
				state1, state2, state3);

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "move_up.png", MAX_TIMEOUT);
		match.click();
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

		Region match = GUIAutomations.findMultipleStateRegion(MIN_TIMEOUT,
				state1, state2, state3);

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "move_down.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Adds a new file to the entry list
	 * 
	 * @param name
	 *            name of the entry
	 * @param path
	 *            path to the file
	 * @throws FindFailed
	 */
	public static void addFile(String name, String path) throws FindFailed {

		openAddDialog();
		fillTextField("name_text_field.png", name);
		fillTextField("file_text_field.png", path);
		saveDialog();
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
			String state3, float similarity) throws FindFailed {
		setMinSimilarity(similarity);
		Region match = findMultipleStateRegion(DEFAULT_TIMEOUT, state1, state2,
				state3);
		setMinSimilarity(DEFAULT_SIMILARITY);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		match.rightClick();
	}

	/**
	 * Opens the add dialog
	 * 
	 * @throws FindFailed
	 */
	public static void openAddDialog() throws FindFailed {

		openPopupMenu("midi_automator.png", null, null, LOW_SIMILARITY);
		SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "add.png", MAX_TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.click();
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
			String state3) throws FindFailed {

		Region match = findMultipleStateRegion(MIN_TIMEOUT, state1, state2,
				state3);
		match.click();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "edit.png", MAX_TIMEOUT);
		match.click();
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
	public static Region findMultipleStateRegion(double timeout,
			String... states) throws FindFailed {

		Region match;
		FindFailed findFailed = null;

		for (String state : states) {
			if (state != null) {
				try {
					match = SikuliAutomation.getSearchRegion().wait(
							screenshotpath + state, timeout);
					return match;
				} catch (FindFailed e) {
					findFailed = e;
					System.out.println(state
							+ " not found. Trying next state...");
				}
			}
		}

		throw findFailed;
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
			String state3) throws FindFailed {

		Region match = findMultipleStateRegion(MIN_TIMEOUT, state1, state2,
				state3);
		match.doubleClick();
	}

	/**
	 * Sets the focus on the Midi Automator window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomator() throws FindFailed {
		try {
			SikuliAutomation.setSearchRegion(findMidiAutomatorRegion());
			Region match = findMultipleStateRegion(DEFAULT_TIMEOUT,
					"Midi_Automator_title.png",
					"Midi_Automator_title_inactive.png");
			match.click(match.offset(50, 20));
		} catch (FindFailed e) {
			System.err.println("focusMidiAutomator() failed");
			throw e;
		}
	}

	/**
	 * Finds the region of the Midi Automator main Window
	 * 
	 * @return the found region
	 * @throws FindFailed
	 */
	public static Region findMidiAutomatorRegion() throws FindFailed {

		try {
			setMinSimilarity(LOW_SIMILARITY);
			SikuliAutomation.setSearchRegion(SCREEN);
			Region searchRegion = findMultipleStateRegion(MAX_TIMEOUT,
					"midi_automator.png");
			setMinSimilarity(DEFAULT_SIMILARITY);
			searchRegion.y = searchRegion.y - 21;
			searchRegion.w = searchRegion.w + 100;
			searchRegion.h = searchRegion.h + 100;
			return searchRegion;
		} catch (FindFailed e) {
			System.err.println("findMidiAutomatorRegion() failed");
			throw e;
		}
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
		try {
			GUIAutomations.checkResult(screenshot, DEFAULT_TIMEOUT);
			fail(error);
		} catch (FindFailed e) {

		} finally {
			setMinSimilarity(DEFAULT_SIMILARITY);
		}
	}

	/**
	 * Checks for the result screenshot
	 * 
	 * @param resultScreenshot
	 *            The screenshot that should be shown
	 * @throws FindFailed
	 */
	public static void checkResult(String resultScreenshot) throws FindFailed {
		checkResult(resultScreenshot, HIGH_SIMILARITY, MAX_TIMEOUT);
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
		checkResult(resultScreenshot, HIGH_SIMILARITY, timeout);
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
		checkResult(resultScreenshot, similarity, MAX_TIMEOUT);
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
			double timeout) throws FindFailed {
		setMinSimilarity(similarity);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + resultScreenshot, timeout);
		setMinSimilarity(DEFAULT_SIMILARITY);
		match.highlight(HIGHLIGHT_DURATION);
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

		Region match = null;

		try {

			// check if file opened
			SikuliAutomation.setSearchRegion(SCREEN);
			match = findMultipleStateRegion(DEFAULT_TIMEOUT, active, inactive);
			match.highlight(HIGHLIGHT_DURATION);

			// close editor
			match.click();
			closeFocusedProgram();

		} catch (FindFailed e) {
			throw e;
		} finally {

			try {
				focusMidiAutomator();
			} catch (FindFailed e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Minimizes the current focused program
	 */
	public static void hideFocusedProgram() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("h", KeyModifier.CMD);
		}
		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type(Key.DOWN, KeyModifier.WIN);
		}
	}

	/**
	 * Closes the current focused program.
	 */
	public static void closeFocusedProgram() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("q", Key.CMD);
		}
		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type(Key.F4, KeyModifier.WIN | KeyModifier.ALT);
		}
	}

	/**
	 * Shows the Midi Automator main window when it was hidden.
	 * 
	 * @throws FindFailed
	 */
	public static void unhideMidiAutomator() throws FindFailed {
		// show Midi Automator
		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type(Key.TAB, Key.CMD);

			// MAc Dock not recognized by Sikuli
			// Region match = SCREEN.wait(screenshotpath
			// + "midi_automator_icon.png", TIMEOUT);
			// match.click();
		}
		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type(Key.TAB, Key.ALT);
			SCREEN.type(Key.TAB, Key.ALT);
		}
	}

	/**
	 * Opens a search file chooser
	 * 
	 * @throws FindFailed
	 */
	public static void openSearchDialog() throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "search_button.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Cancels a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void cancelDialog() throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "cancel_button.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Saves a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void saveDialog() throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "save_button.png", MAX_TIMEOUT);
		match.click();
	}

	/**
	 * Fills a text field with a given text
	 * 
	 * @param screenshot
	 *            screenshot of the text field
	 * @param text
	 *            the text to type in
	 * @throws FindFailed
	 */
	public static void fillTextField(String screenshot, String text)
			throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + screenshot, MAX_TIMEOUT);
		match.click(match.offset(50, 0));

		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("a", KeyModifier.CMD);
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type("a", KeyModifier.CTRL);
		}
		SCREEN.paste(text);
	}

	/**
	 * Closes the Midi Automator program
	 * 
	 * @throws FindFailed
	 */
	public static void closeMidiAutomator() throws FindFailed {

		focusMidiAutomator();
		closeFocusedProgram();
	}

	/**
	 * Restarts the MIDI Automator and sets search region to it
	 * 
	 * @throws FindFailed
	 * @throws IOException
	 */
	public static void restartMidiAutomator() throws FindFailed, IOException {

		closeMidiAutomator();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		GUIAutomations.openMidiAutomator();
	}

}
