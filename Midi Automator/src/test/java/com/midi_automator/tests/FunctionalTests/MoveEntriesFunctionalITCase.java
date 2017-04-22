package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.event.KeyEvent;

import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.menus.MainFramePopupMenu;

public class MoveEntriesFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void moveUpDownMenuShouldBeDisabledIfListIsEmpty() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// check for disabled menu entry
		JPopupMenuFixture popupMenu = getFileList().showPopupMenu();

		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_UP)
				.requireDisabled();
		popupMenu.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_DOWN)
				.requireDisabled();

	}

	@Test
	public void entryShouldBeMovedDownByMenu() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_312.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move down entry
		moveDownEntry(0);
		moveDownEntry(1);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 1");
		assertEquals((getFileList().contents())[1], "2 Hello World 2");
		assertEquals((getFileList().contents())[2], "3 Hello World 3");

		// check for inactive menu at last item
		getFileList().showPopupMenuAt(2)
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_DOWN)
				.requireDisabled();
	}

	@Test
	public void entryShouldBeMovedDownByGlobalKeyListener() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_312.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move down entry
		selectEntryByLeftClick(0);
		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_DOWN);
		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_DOWN);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 1");
		assertEquals((getFileList().contents())[1], "2 Hello World 2");
		assertEquals((getFileList().contents())[2], "3 Hello World 3");

		// check for inactive menu at last item
		getFileList().showPopupMenuAt(2)
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_DOWN)
				.requireDisabled();

		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}

	@Test
	public void entryShouldBeMovedUpByGlobalKeyListener() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move up entry
		selectEntryByLeftClick(2);
		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_UP);
		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_UP);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 3");
		assertEquals((getFileList().contents())[1], "2 Hello World 1");
		assertEquals((getFileList().contents())[2], "3 Hello World 2");

		// check for inactive menu on first item
		getFileList().showPopupMenuAt(0)
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_UP)
				.requireDisabled();

		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}

	@Test
	public void entryShouldBeMovedUpByMenu() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move up entry
		moveUpEntry(2);
		moveUpEntry(1);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 3");
		assertEquals((getFileList().contents())[1], "2 Hello World 1");
		assertEquals((getFileList().contents())[2], "3 Hello World 2");

		// check for inactive menu on first item
		getFileList().showPopupMenuAt(0)
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_MOVE_UP)
				.requireDisabled();
	}

	@Test
	public void entryShouldBeMovedDownByDND() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_312.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move entry down
		getFileList().drag(0);
		getFileList().drop(2);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 1");
		assertEquals((getFileList().contents())[1], "2 Hello World 2");
		assertEquals((getFileList().contents())[2], "3 Hello World 3");
	}

	@Test
	public void entryShouldBeMovedUpByDND() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_123.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move up entry
		getFileList().drag(2);
		getFileList().drop(0);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 1");
		assertEquals((getFileList().contents())[1], "2 Hello World 3");
		assertEquals((getFileList().contents())[2], "3 Hello World 2");
	}

	@Test
	public void emptyEntryShouldBeMovedUpByDND() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_world_12SPACE.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// move up entry
		getFileList().drag(2);
		getFileList().drop(0);

		// check for correct order
		assertEquals((getFileList().contents())[0], "1 Hello World 1");
		assertEquals((getFileList().contents())[1], "2  ");
		assertEquals((getFileList().contents())[2], "3 Hello World 2");
	}
}
