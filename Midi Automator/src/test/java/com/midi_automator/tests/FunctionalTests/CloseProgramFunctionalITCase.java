package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class CloseProgramFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void comittingTrayDialogShouldHideProgram() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// set minimize on close
		DialogFixture preferencesDialog = openPreferences();
		checkMinimizeOnClose(true, preferencesDialog);
		saveDialog(preferencesDialog);

		window.close();
		window.requireNotVisible();

	}
}
