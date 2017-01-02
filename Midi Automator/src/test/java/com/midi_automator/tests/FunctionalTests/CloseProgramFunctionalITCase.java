package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.findTrayInfoPane;

import org.assertj.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

public class CloseProgramFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void closingProgramShouldShowDialogOnWindows() {

		if (System.getProperty("os.name").contains("Windows")) {
			startApplication();
			window.close();

			JOptionPaneFixture trayOptionPane = findTrayInfoPane();
			trayOptionPane.requireVisible();
		}
	}

	@Test
	public void comittingTrayDialogShouldHideProgramOnWindows() {

		if (System.getProperty("os.name").contains("Windows")) {
			startApplication();
			window.close();

			JOptionPaneFixture trayOptionPane = findTrayInfoPane();
			trayOptionPane.button().click();

			window.requireNotVisible();
		}
	}
}
