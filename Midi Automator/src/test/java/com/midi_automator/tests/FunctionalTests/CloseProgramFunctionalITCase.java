package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.assertj.swing.fixture.JOptionPaneFixture;
import org.junit.Test;

public class CloseProgramFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void closingProgramShouldShowDialogOnWindows() {

		startApplication();
		window.close();

		JOptionPaneFixture trayOptionPane = findTrayInfoPane();
		trayOptionPane.requireVisible();
	}

	@Test
	public void comittingTrayDialogShouldHideProgramOnWindows() {

		startApplication();
		window.close();

		JOptionPaneFixture trayOptionPane = findTrayInfoPane();
		trayOptionPane.button().click();

		window.requireNotVisible();

	}
}
