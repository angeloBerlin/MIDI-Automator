package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class SwitchButtonsFunctionalITCase extends GUITestCase {

	@Test
	public void nextAndPrevBttonsNotActiveOnEmptyList() {

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
		checkIfEntryWasOpened("Hello World 1");

		// open second file
		nextFile();
		checkIfEntryWasOpened("Hello World 2");

		// cycle first file
		nextFile();
		checkIfEntryWasOpened("Hello World 1");
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// open first file
		nextFile();
		checkIfEntryWasOpened("Hello World 1");

		// delete second file
		deleteEntry(1);

		// open third file
		nextFile();
		checkIfEntryWasOpened("Hello World 3");

		// cycle first file
		nextFile();
		checkIfEntryWasOpened("Hello World 1");
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterAddingOnIndex2() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// open first file
		nextFile();
		checkIfEntryWasOpened("Hello World 1");

		// open second file
		nextFile();
		checkIfEntryWasOpened("Hello World 2");

		// add third file
		addFile("Hello World 3", currentPath + "/testfiles/Hello World 3.rtf");

		// open third file
		nextFile();
		checkIfEntryWasOpened("Hello World 3");

		// cycle first file
		nextFile();
		checkIfEntryWasOpened("Hello World 1");
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle second file
		prevFile();
		checkIfEntryWasOpened("Hello World 2");

		// open first file
		prevFile();
		checkIfEntryWasOpened("Hello World 1");
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle third file
		prevFile();
		checkIfEntryWasOpened("Hello World 3");

		// delete second file
		deleteEntry(1);

		// open third file
		prevFile();
		checkIfEntryWasOpened("Hello World 3");
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterAddingOnIndex2() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// cycle second file
		prevFile();
		checkIfEntryWasOpened("Hello World 2");

		// open first file
		prevFile();
		checkIfEntryWasOpened("Hello World 1");

		// add third file
		addFile("Hello World 3", currentPath + "/testfiles/Hello World 3.rtf");

		// cycle third file
		prevFile();
		checkIfEntryWasOpened("Hello World 3");
	}
}
