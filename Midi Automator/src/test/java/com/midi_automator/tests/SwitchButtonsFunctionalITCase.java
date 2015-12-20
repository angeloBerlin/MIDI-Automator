package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class SwitchButtonsFunctionalITCase extends GUITestCase {

	@Test
	public void nextAndPrevButtonsNotActiveOnEmptyList() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// check for inactive buttons
		getNextButton().requireDisabled();
		getPrevButton().requireDisabled();
	}

	@Test
	public void nextFileShouldBeOpenedInCycle() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// open first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");

		// open second file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 2");
		sikulix.checkIfFileOpened("Hello_World_2_RTF.png",
				"Hello_World_2_RTF_inactive.png");

		// cycle first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// open first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");

		// delete second file
		deleteEntry(1);

		// open third file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 3");
		sikulix.checkIfFileOpened("Hello_World_3_RTF.png",
				"Hello_World_3_RTF_inactive.png");

		// cycle first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterAddingOnIndex2() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// open first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");

		// open second file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 2");
		sikulix.checkIfFileOpened("Hello_World_2_RTF.png",
				"Hello_World_2_RTF_inactive.png");

		// add third file
		addFile("Hello World 3", currentPath + "/testfiles/Hello World 3.rtf");

		// open third file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 3");
		sikulix.checkIfFileOpened("Hello_World_3_RTF.png",
				"Hello_World_3_RTF_inactive.png");

		// cycle first file
		nextFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle second file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 2");
		sikulix.checkIfFileOpened("Hello_World_2_RTF.png",
				"Hello_World_2_RTF_inactive.png");

		// open first file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle third file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 3");
		sikulix.checkIfFileOpened("Hello_World_3_RTF.png",
				"Hello_World_3_RTF_inactive.png");

		// delete second file
		deleteEntry(1);

		// open third file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 3");
		sikulix.checkIfFileOpened("Hello_World_3_RTF.png",
				"Hello_World_3_RTF_inactive.png");
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterAddingOnIndex2() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle second file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 2");
		sikulix.checkIfFileOpened("Hello_World_2_RTF.png",
				"Hello_World_2_RTF_inactive.png");

		// open first file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 1");
		sikulix.checkIfFileOpened("Hello_World_1_RTF.png",
				"Hello_World_1_RTF_inactive.png");

		// add third file
		addFile("Hello World 3", currentPath + "/testfiles/Hello World 3.rtf");

		// cycle third file
		prevFile();
		checkIfOpenEntryIsDisplayed("Hello World 3");
		sikulix.checkIfFileOpened("Hello_World_3_RTF.png",
				"Hello_World_3_RTF_inactive.png");
	}
}
