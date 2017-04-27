package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.junit.Test;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.tests.utils.MockUpUtils;

public class SwitchButtonsFunctionalITCase extends FunctionalBaseCase {

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
	public void clickNextFileShouldBeOpenedInCycle() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// open first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// open second file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

			// cycle first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// open first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// delete second file
			deleteEntry(1);

			// open first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// cycle first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 3");
			sikulix.checkIfFileOpened("Hello_World_3_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nextThirdFileShouldBeOpenedAfterAddingOnIndex2() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// open first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// open second file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

			// add third file
			saveDialog(addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf", ""));

			// open third file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 3");
			sikulix.checkIfFileOpened("Hello_World_3_RTF.png");

			// cycle first file
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void prevFileShouldBeOpenedInCycle() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// cycle second file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

			// open first file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void prevThirdFileShouldBeOpenedAfterDeletingSecondFile() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_123.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// cycle third file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 3");
			sikulix.checkIfFileOpened("Hello_World_3_RTF.png");

			// delete second file
			deleteEntry(1);

			// open third file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void prevFirstFileShouldBeOpenedAfterAddingOnIndex2() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			// cycle second file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 2");
			sikulix.checkIfFileOpened("Hello_World_2_RTF.png");

			// open first file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

			// add third file
			saveDialog(addFile("Hello World 3", currentPath
					+ "/testfiles/Hello World 3.rtf", ""));

			// cycle third file
			clickPrevFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			sikulix.checkIfFileOpened("Hello_World_1_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void onlyThirdFileShouldBeOpenedOnFastSwitching() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// fast switch to third file
		try {
			clickNextFile();
			checkIfOpenEntryIsNotDisplayed("Hello World 1");

			clickNextFile();
			checkIfOpenEntryIsNotDisplayed("Hello World 2");

			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 2);
			checkIfOpenEntryIsDisplayed("Hello World 3");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
