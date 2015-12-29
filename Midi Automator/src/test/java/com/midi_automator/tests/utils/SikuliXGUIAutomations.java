package com.midi_automator.tests.utils;

import java.io.IOException;

import org.assertj.core.api.Fail;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.midi_automator.utils.SystemUtils;

public class SikuliXGUIAutomations extends SikuliXAutomation {

	public SikuliXGUIAutomations(Screen screen) {
		super(screen);
	}

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
		Region match = SikuliXGUIAutomations.findMultipleStateRegion(
				DEFAULT_TIMEOUT, "NSIS_cancel_button.png");
		match.click();
	}

	/**
	 * Closes the NSIS installer
	 * 
	 * @throws FindFailed
	 */
	public static void closeNSISInstaller() throws FindFailed {
		Region match = SikuliXGUIAutomations.findMultipleStateRegion(
				DEFAULT_TIMEOUT, "NSIS_close_button_active");
		match.click();
	}

	/**
	 * Finds the region of the Midi Automator installer main Window
	 * 
	 * @return the found region
	 * @throws FindFailed
	 */
	public static Region findMidiAutomatorInstallerRegion() throws FindFailed {

		try {
			setMinSimilarity(LOW_SIMILARITY);
			SikuliXAutomation.setSearchRegion(SCREEN);
			Region searchRegion = findMultipleStateRegion(MAX_TIMEOUT,
					"midi_automator_installer.png");
			setMinSimilarity(DEFAULT_SIMILARITY);
			searchRegion.y -= 41;
			searchRegion.h += 41;
			return searchRegion;
		} catch (FindFailed e) {
			System.err.println("findMidiAutomatorInstallerRegion() failed");
			throw e;
		}
	}

	/**
	 * Sets the focus on the Midi Automator installer window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomatorInstaller() throws FindFailed {
		try {
			SikuliXAutomation
					.setSearchRegion(findMidiAutomatorInstallerRegion());
			Region match = findMultipleStateRegion(DEFAULT_TIMEOUT,
					"Midi_Automator_installer_title.png",
					"Midi_Automator_installer_title_inactive.png");
			match.click(match.offset(50, 20));
		} catch (FindFailed e) {
			System.err.println("focusMidiAutomatorInstaller() failed");
			throw e;
		}
	}

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
	 * Sets the focus on the Midi Automator window
	 * 
	 * @throws FindFailed
	 */
	public static void focusMidiAutomator() throws FindFailed {
		try {
			SikuliXAutomation.setSearchRegion(findMidiAutomatorRegion());
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
			SikuliXAutomation.setSearchRegion(SCREEN);
			Region searchRegion = findMultipleStateRegion(MAX_TIMEOUT,
					"midi_automator.png");
			setMinSimilarity(DEFAULT_SIMILARITY);
			searchRegion.y = searchRegion.y - 21;
			searchRegion.w = searchRegion.w + 500;
			searchRegion.h = searchRegion.h + 100;
			return searchRegion;
		} catch (FindFailed e) {
			System.err.println("findMidiAutomatorRegion() failed");
			throw e;
		}
	}

	/**
	 * Checks if the file opened correctly
	 * 
	 * @param active
	 *            screenshot of active window
	 * @param inactive
	 *            screenshot of inactive window
	 */
	public void checkIfFileOpened(String active, String inactive) {

		Region match = null;

		try {

			// check if file opened
			SikuliXAutomation.setSearchRegion(SCREEN);
			match = findMultipleStateRegion(DEFAULT_TIMEOUT, active, inactive);

			// close editor
			match.click();
			closeFocusedProgram();

		} catch (FindFailed e) {
			Fail.fail("File did not open.", e);
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
	 * Closes the Midi Automator program
	 * 
	 * @throws FindFailed
	 */
	public static void closeMidiAutomator() throws FindFailed {

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
	public static void restartMidiAutomator() throws FindFailed, IOException {

		closeMidiAutomator();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SikuliXGUIAutomations.openMidiAutomator();
	}

}
