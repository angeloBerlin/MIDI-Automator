package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.openEntryByDoubleClick;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class OpenItemFileFunctionalITCase extends GUITestCase {

	@Test
	public void helloWorldFileShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World");
		sikulix.checkIfFileOpened("Hello_World_RTF.png");
	}

	@Test
	public void helloWorldUTF8FileShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_ÄÖÜ.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World ÄÖÜ");
		sikulix.checkIfFileOpened("Hello_World_ÄÖÜ_RTF.png");
	}

	@Test
	public void openingAnEmptyFileShallNotShowAFailure() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(1);

		checkEmptyInfoText();
	}
}
