package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.menus.MainFramePopupMenu;

public class DeleteFileFunctionalITCase extends FunctionalBaseCase {

	private String helloWorldMido;

	public DeleteFileFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			helloWorldMido = "Hello_World_MAC.mido";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			helloWorldMido = "Hello_World_Windows.mido";
		}
	}

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

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		deleteEntry(0);

		assertEquals(GUIAutomations.getFileList().contents().length, 0);
	}
}
