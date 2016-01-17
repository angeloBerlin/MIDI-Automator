package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class OpenItemFileFunctionalITCase extends FunctionalBaseCase {

	private String helloWorldMido;
	private String defaultProgramScreenshot;
	private String specificProgramScreenshot;

	public OpenItemFileFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			helloWorldMido = "Hello_World_MAC.mido";
			defaultProgramScreenshot = "TextEdit.png";
			specificProgramScreenshot = "Word.png";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			helloWorldMido = "Hello_World_Windows.mido";
			defaultProgramScreenshot = "Wordpad.png";
			specificProgramScreenshot = "Notepad.png";
		}
	}

	@Test
	public void helloWorldFileShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World");
		sikulix.checkIfProgramOpened(defaultProgramScreenshot);
		sikulix.checkIfFileOpened("Hello_World_RTF.png");
	}

	@Test
	public void helloWorldFileShouldBeOpenedWithSpecificProgram() {

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World");
		sikulix.checkIfProgramOpened(specificProgramScreenshot);
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
