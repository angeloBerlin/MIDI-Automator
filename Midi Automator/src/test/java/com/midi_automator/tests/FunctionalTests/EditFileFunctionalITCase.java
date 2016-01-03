package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.cancelDialog;
import static com.midi_automator.tests.utils.GUIAutomations.getFileList;
import static com.midi_automator.tests.utils.GUIAutomations.openEditDialog;
import static com.midi_automator.tests.utils.GUIAutomations.openEntryByDoubleClick;
import static com.midi_automator.tests.utils.GUIAutomations.openFileListPopupMenu;
import static com.midi_automator.tests.utils.GUIAutomations.openSearchDialog;
import static com.midi_automator.tests.utils.GUIAutomations.saveDialog;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.frames.EditFrame;

public class EditFileFunctionalITCase extends FunctionalITCase {

	@Test
	public void editMenuShouldBeDisabledIfListIsEmpty() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		JPopupMenuFixture popupMenu = openFileListPopupMenu();

		JMenuItemFixture editMenuItem = popupMenu
				.menuItem(MainFramePopupMenu.NAME_MENU_ITEM_EDIT);

		editMenuItem.requireDisabled();
	}

	@Test
	public void editingFileShouldBeCanceled() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText("x");
		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialog(editFrame);

		assertEquals(getFileList().contents()[0], "1 Hello World");
	}

	@Test
	public void helloWorldShouldBeEdited() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText(
				"Hello World Edit");
		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText(
				currentPath + "/testfiles/Hello World edit.rtf");
		saveDialog(editFrame);

		assertEquals(getFileList().contents()[0], "1 Hello World Edit");

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World Edit");
		sikulix.checkIfFileOpened("Hello_World_Edit_RTF.png");
	}

	// @Test
	public void fileChooserOfEditDialogShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		JFileChooserFixture fileChooser = openSearchDialog(editFrame);

		fileChooser.setCurrentDirectory(new File(currentPath + "/testfiles"));
		// TODO: problem with select file on file chooser
		fileChooser.selectFile(new File(currentPath
				+ "/testfiles/Hello World.rtf"));
		fileChooser.approve();

		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).requireText(
				currentPath + "/testfiles/Hello World.rtf");
	}

	@Test
	public void sendingMidiSignatureShouldBeShown() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.label(EditFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL)
				.requireText("channel 16: CONTROL CHANGE 1 value: 127");

		cancelDialog(editFrame);

		editFrame = openEditDialog(1);
		editFrame.label(EditFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL)
				.requireText("channel 16: CONTROL CHANGE 2 value: 127");
	}
}
