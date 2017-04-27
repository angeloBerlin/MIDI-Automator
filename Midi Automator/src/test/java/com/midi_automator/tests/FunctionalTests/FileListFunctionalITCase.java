package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Assume;
import org.junit.Test;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;

public class FileListFunctionalITCase extends FunctionalBaseCase {

	private int popupMenuKey;

	public FileListFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			popupMenuKey = KeyEvent.VK_META;
		}

		if (System.getProperty("os.name").contains("Windows")) {
			popupMenuKey = KeyEvent.VK_CONTEXT_MENU;
		}
	}

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
	public void item128ShouldeVisibleIfDecreasedFromItem1ToItem125() {

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
	public void item2ShouldScrollToTopIfItem3WasDecreased() {

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
	public void item8ShouldScrollToTopIfItem7WasIncreased() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();
		try {
			selectEntryByLeftClick(6);
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			clickNextFile();

			if (!sikulix.checkforStates("selected_Hello_World_9.png")) {
				fail("Incorrect scrolling");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void nextItemShouldBeOpenedOnKeyStroke() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {
			pressKeyOnMainFrame(KeyEvent.VK_SPACE);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");

			pressKeyOnMainFrame(KeyEvent.VK_RIGHT);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 2");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void selectedItemShouldBeOpenedOnKeyStroke() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {
			pressKeyOnMainFrame(KeyEvent.VK_ENTER);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			getFileList().requireSelection(0);

			pressKeyOnMainFrame(KeyEvent.VK_ENTER);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");
			getFileList().requireSelection(0);

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
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 3");

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_LEFT);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 2");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void nextItemAfterSelectionShallBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_123_no_file.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		try {

			selectEntryByLeftClick(1);

			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_RIGHT);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			pressAndReleaseKeysOnMainFrame(KeyEvent.VK_RIGHT);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
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
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			checkIfOpenEntryIsDisplayed("Hello World 1");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void popUpMenuShallBeShownOnSelectedItemOnKeyStroke() {

		// issue in sending keys on mac
		Assume.assumeFalse(System.getProperty("os.name").equals("Mac OS X"));

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		JListFixture fileList = getFileList();
		JPopupMenuFixture popupMenu = new JPopupMenuFixture(robot,
				ctx.getBean(MainFramePopupMenu.class));
		try {
			// popup menu on item 1 if none is selected
			pressAndReleaseKeysOnMainFrame(popupMenuKey);
			popupMenu.requireVisible();
			fileList.requireSelection(0);

			// popup menu on item 4
			selectEntryByLeftClick(3);
			pressAndReleaseKeysOnMainFrame(popupMenuKey);
			popupMenu.requireVisible();
			fileList.requireSelection(3);

			// popup menu on item 8
			selectEntryByLeftClick(7);
			clickNextFile();
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT + 50);
			pressAndReleaseKeysOnMainFrame(popupMenuKey);
			popupMenu.requireVisible();
			fileList.requireSelection(8);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
