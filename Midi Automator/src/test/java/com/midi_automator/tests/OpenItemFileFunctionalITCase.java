package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class OpenItemFileFunctionalITCase extends GUITestCase {

	@Test
	public void helloWorldFileShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		// check if file opened
		checkIfEntryWasOpened("Hello World");
	}

	@Test
	public void helloWorldÄÖÜFileShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_ÄÖÜ.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		// check if file opened
		checkIfEntryWasOpened("Hello World ÄÖÜ");
	}

	@Test
	public void openingAnEmptyFileShallNotShowAFailure() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(1);

		// check for empty info text
		checkEmptyInfoText();
	}
}
