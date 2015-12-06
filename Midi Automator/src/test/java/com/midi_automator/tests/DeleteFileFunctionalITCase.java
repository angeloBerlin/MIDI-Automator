package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.MainFramePopupMenu;

public class DeleteFileFunctionalITCase extends GUITestCase {

	@Test
	public void deleteMenuShouldBeDisabledIfListIsEmpty() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		JPopupMenuFixture popupMenu = openFileListPopupMenu();

		JMenuItemFixture deleteMenuItem = popupMenu
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_DELETE);

		deleteMenuItem.requireDisabled();
	}

	@Test
	public void helloWorldEntryShouldBeDeleted() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		startApplication();

		deleteEntry(0);

		assertEquals(GUIAutomations.getFileList().contents().length, 0);
	}
}
