package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.deleteEntry;
import static com.midi_automator.tests.utils.GUIAutomations.openFileListPopupMenu;
import static org.junit.Assert.assertEquals;

import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.MainFramePopupMenu;

public class DeleteFileFunctionalITCase extends FunctionalITCase {

	@Test
	public void deleteMenuShouldBeDisabledIfListIsEmpty() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		JPopupMenuFixture popupMenu = openFileListPopupMenu();

		JMenuItemFixture deleteMenuItem = popupMenu
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_DELETE);

		deleteMenuItem.requireDisabled();
	}

	@Test
	public void helloWorldEntryShouldBeDeleted() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		deleteEntry(0);

		assertEquals(GUIAutomations.getFileList().contents().length, 0);
	}
}
