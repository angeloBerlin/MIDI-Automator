package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.junit.Test;

import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.tests.utils.MockUpUtils;

public class FileListFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void item2ShouldScrollToTopIfSelected() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(1);

		if (!sikulix.checkforStates("selected_Hello_World_2.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item128ShouldbeVisibleIfItem125IsSelected() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(124);

		if (!sikulix.checkforStates("selected_Hello_World_125.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item128ShouldbeVisibleIfDecreasedFromItem1ToItem125() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		clickPrevFile();
		clickPrevFile();
		clickPrevFile();
		clickPrevFile();

		if (!sikulix.checkforStates("selected_Hello_World_125.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item2ShoulScrollToTopIfItem3WasDecreased() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(2);
		clickPrevFile();

		if (!sikulix.checkforStates("selected_Hello_World_2.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void nextFileShouldBeOpenedOnKeyStroke() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {
			pressKeyOnMainFrame(KeyEvent.VK_SPACE);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");

			pressKeyOnMainFrame(KeyEvent.VK_ENTER);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 2");

			pressKeyOnMainFrame(KeyEvent.VK_RIGHT);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 3");

			pressKeyOnMainFrame(KeyEvent.VK_DOWN);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void previousFileShouldBeOpenedOnKeyStroke() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {
			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_BACK_SPACE);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 3");

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_LEFT);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 2");

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_UP);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nextFileAfterSelectionShallBeChosen() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {

			selectEntryByLeftClick(2);

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_RIGHT);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void previousFileBeforeSelectionShallBeChosen() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {

			selectEntryByLeftClick(1);

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_LEFT);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
