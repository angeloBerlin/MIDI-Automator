package de.tieffrequent.midi.automator.tests;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.Utils;

public class GUIAutomations extends SikuliAutomation {

	/**
	 * Opens the Midi Automator program
	 * 
	 * @throws IOException
	 */
	public static void openMidiAutomator() throws IOException {

		String filePath = "";

		if (System.getProperty("os.name").equals("Mac OS X")) {
			filePath = "/Applications/Midi Automator.app";
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			filePath = Utils
					.replaceSystemVariables("%ProgramFiles%\\Midi Automator\\Midi Automator.exe");
		}

		File file = new File(filePath);

		Desktop.getDesktop().open(file);
	}

	/**
	 * Puts the program to midi learn for the given screenshot
	 * 
	 * @param screenshot
	 *            A screenshot to open midi learn on
	 * @throws FindFailed
	 */
	public static void midiLearn(String screenshot) throws FindFailed {
		openPopupMenu(screenshot);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_learn.png", TIMEOUT);
		match.click();
	}

	/**
	 * Unlearns a midi message on the given screenshot
	 * 
	 * @param screenshot
	 *            A screenshot to open midi learn on
	 * @throws FindFailed
	 */
	public static void midiUnlearn(String screenshot) throws FindFailed {
		openPopupMenu(screenshot);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_unlearn.png", TIMEOUT);
		match.click();
	}

	/**
	 * Cancels the midi learn for the given screenshot
	 * 
	 * @param screenshot
	 *            A screenshot to open midi learn on
	 * @throws FindFailed
	 */
	public static void cancelMidiLearn(String screenshot) throws FindFailed {
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + screenshot, TIMEOUT);
		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "cancel_midi_learn.png", TIMEOUT);
		match.click();
	}

	/**
	 * Opens the preferences window.
	 * 
	 * @return the region of the preferences window
	 * @throws FindFailed
	 */
	public static Region openPreferences() throws FindFailed {
		openFileMenu();
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "preferences.png", TIMEOUT);
		match.click();
		SikuliAutomation.setSearchRegion(SCREEN);
		SikuliAutomation.setMinSimilarity(LOW_SIMILARITY);
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "midi_automator_preferences.png", TIMEOUT);
		SikuliAutomation.setMinSimilarity(DEFAULT_SIMILARITY);
		match.y -= 20;
		match.h += 20;
		return match;
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

		Region preferencesRegion = GUIAutomations.openPreferences();
		SikuliAutomation.setSearchRegion(preferencesRegion);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scLabel, TIMEOUT);
		match.click(match.offset(0, 20));
		match.y += 20;
		match.w = 200;
		match.h = 100;
		SikuliAutomation.setSearchRegion(match);
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + scValue, TIMEOUT);
		match.click();
		SikuliAutomation.setSearchRegion(preferencesRegion);
		GUIAutomations.saveDialog();
		SikuliAutomation.setSearchRegion(GUIAutomations
				.findMidiAutomatorRegion());
	}

	/**
	 * Opens the next file
	 * 
	 * @throws FindFailed
	 */
	public static void nextFile() throws FindFailed {
		SikuliAutomation.setMinSimilarity(HIGH_SIMILARITY);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "next.png", TIMEOUT);
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
				screenshotpath + "prev.png", TIMEOUT);
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
				screenshotpath + "file.png", TIMEOUT);
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
				screenshotpath + "exit.png", TIMEOUT);
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
		Region match = findMultipleStateRegion(state1, state2, state3);
		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "delete.png", TIMEOUT);
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

		Region match = GUIAutomations.findMultipleStateRegion(state1, state2,
				state3);

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "move_up.png", TIMEOUT);
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

		Region match = GUIAutomations.findMultipleStateRegion(state1, state2,
				state3);

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "move_down.png", TIMEOUT);
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
	 * @param screenshot
	 *            The screenshot of the popup component
	 * @throws FindFailed
	 */
	public static void openPopupMenu(String screenshot) throws FindFailed {
		setMinSimilarity(0.8f);
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + screenshot, TIMEOUT);
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

		openPopupMenu("midi_automator.png");
		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "add.png", TIMEOUT);
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

		Region match = findMultipleStateRegion(state1, state2, state3);
		match.click();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		match.rightClick();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "edit.png", TIMEOUT);
		match.click();
	}

	/**
	 * Finds a region that can have multiple states, i.e. active, inactive,
	 * unfocused
	 * 
	 * @param states
	 *            the different states of the region
	 * @return The found region
	 * @throws FindFailed
	 */
	public static Region findMultipleStateRegion(String... states)
			throws FindFailed {

		Region match;
		FindFailed findFailed = null;

		for (String state : states) {
			try {
				match = SikuliAutomation.getSearchRegion().wait(
						screenshotpath + state, MIN_TIMEOUT);
				return match;
			} catch (FindFailed e) {
				findFailed = e;
				System.out.println(state + " not found. Trying next state...");
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

		Region match = findMultipleStateRegion(state1, state2, state3);

		// activate
		match.click();
		match.doubleClick();
		// strange workaround to open
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		match.doubleClick();
	}

	/**
	 * Sets the focus on the Midi Automator window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomator() throws FindFailed {
		Region match = findMultipleStateRegion("Midi_Automator_title.png",
				"Midi_Automator_title_inactive.png");
		match.click();
	}

	/**
	 * Finds the region of the Midi Automator main Window
	 * 
	 * @return the found region
	 * @throws FindFailed
	 */
	public static Region findMidiAutomatorRegion() throws FindFailed {

		setMinSimilarity(0.8f);
		Region searchRegion = SCREEN.wait(
				screenshotpath + "midi_automator.png", TIMEOUT);
		setMinSimilarity(DEFAULT_SIMILARITY);
		searchRegion.y = searchRegion.y - 22;
		searchRegion.w = searchRegion.w + 100;
		searchRegion.h = searchRegion.h + 100;
		return searchRegion;
	}

	/**
	 * Checks if the file opened correctly
	 * 
	 * @param screenshot
	 *            screenshot to search for after opening
	 * @return <TRUE> if file was opened, <FALSE> if not
	 */
	public static boolean checkIfFileOpened(String screenshot) {

		Region match = null;

		try {

			// // minimize Midi Automator
			focusMidiAutomator();

			if (System.getProperty("os.name").equals("Mac OS X")) {
				SCREEN.type("h", KeyModifier.CMD);
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				SCREEN.type(Key.DOWN, KeyModifier.WIN);
			}

			// check if file opened
			SikuliAutomation.setSearchRegion(SCREEN);

			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + screenshot, TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// close editor
			match.click();
			if (System.getProperty("os.name").equals("Mac OS X")) {
				SCREEN.type("q", Key.CMD);
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				SCREEN.type(Key.F4, KeyModifier.WIN | KeyModifier.ALT);
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (FindFailed e) {
			return false;
		} finally {

			// show Midi Automator
			if (System.getProperty("os.name").equals("Mac OS X")) {
				SCREEN.type(Key.TAB, Key.CMD);
			}
			if (System.getProperty("os.name").equals("Windows 7")) {
				SCREEN.type(Key.TAB, Key.ALT);
				SCREEN.type(Key.TAB, Key.ALT);
			}
		}
		return true;
	}

	/**
	 * Opens a search file chooser
	 * 
	 * @throws FindFailed
	 */
	public static void openSearchDialog() throws FindFailed {

		Region match;

		// open search dialog
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "search_button.png", TIMEOUT);
		match.click();
		match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "file_chooser.png", TIMEOUT);
		match.highlight(HIGHLIGHT_DURATION);

		// cancel
		if (System.getProperty("os.name").equals("Mac OS X")) {
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "cancel_button.png", TIMEOUT);
			match.click();
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			match = SikuliAutomation.getSearchRegion().wait(
					screenshotpath + "abbrechen_button.png", TIMEOUT);
			match.click();
		}
	}

	/**
	 * Cancels a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void cancelDialog() throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "cancel_button.png", TIMEOUT);
		match.click();
	}

	/**
	 * Saves a dialog
	 * 
	 * @throws FindFailed
	 */
	public static void saveDialog() throws FindFailed {

		Region match = SikuliAutomation.getSearchRegion().wait(
				screenshotpath + "save_button.png", TIMEOUT);
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
				screenshotpath + screenshot, TIMEOUT);
		match.click();

		if (System.getProperty("os.name").equals("Mac OS X")) {
			SCREEN.type("a", KeyModifier.CMD);
		}

		if (System.getProperty("os.name").equals("Windows 7")) {
			SCREEN.type("a", KeyModifier.CTRL);
		}
		SCREEN.paste(text);
	}

	/**
	 * Restarts the MIDI Automator and sets search region to it
	 * 
	 * @throws FindFailed
	 * @throws IOException
	 */
	public static void restartMidiAutomator() throws FindFailed, IOException {

		GUIAutomations.openExitMenu();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		GUIAutomations.openMidiAutomator();

		SikuliAutomation.setSearchRegion(GUIAutomations
				.findMidiAutomatorRegion());

	}
}
