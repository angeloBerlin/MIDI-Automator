package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.io.File;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Test;

import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.MainFramePopupMenu;
import com.midi_automator.view.frames.AddFrame;
import com.midi_automator.view.frames.EditFrame;

public class EditFileFunctionalITCase extends FunctionalBaseCase {

	private String helloWorldMido;
	private String editedProgramPath;
	private String editedProgramScreenshot;

	public EditFileFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			helloWorldMido = "Hello_World_MAC.mido";
			editedProgramPath = "/Applications/Microsoft Office 2011/Microsoft Word.app";
			editedProgramScreenshot = "Word.png";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			helloWorldMido = "Hello_World_Windows.mido";
			editedProgramPath = "C:/Windows/System32/notepad.exe";
			editedProgramScreenshot = "Notepad.png";
		}
	}

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

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText("x");
		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialog(editFrame);

		assertEquals(getFileList().contents()[0], "1 Hello World");
	}

	@Test
	public void editingFileShouldBeCanceledByEnterCancelButton() {

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText("x");
		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialogByEnter(editFrame);

		assertEquals(getFileList().contents()[0], "1 Hello World");
	}

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			FrameFixture editFrame = openEditDialog(0);
			editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText(
					"Hello World Edit");
			editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText(
					currentPath + "/testfiles/Hello World edit.rtf");
			editFrame.textBox(EditFrame.NAME_PROGRAM_TEXT_FIELD).setText(
					editedProgramPath);
			saveDialog(editFrame);

			assertEquals(getFileList().contents()[0], "1 Hello World Edit");

			openEntryByDoubleClick(0);
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT * 3);

			checkIfOpenEntryIsDisplayed("Hello World Edit");
			sikulix.checkIfProgramOpened(editedProgramScreenshot);
			sikulix.checkIfFileOpened("Hello_World_Edit_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void helloWorldShouldBeEditedOnEnterSaveButton() {

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		editFrame.textBox(EditFrame.NAME_NAME_TEXT_FIELD).setText(
				"Hello World Edit");
		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).setText(
				currentPath + "/testfiles/Hello World edit.rtf");
		editFrame.textBox(EditFrame.NAME_PROGRAM_TEXT_FIELD).setText(
				editedProgramPath);
		saveDialogByEnter(editFrame);

		assertEquals(getFileList().contents()[0], "1 Hello World Edit");
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture editFrame = openEditDialog(0);
		JFileChooserFixture fileChooser = openSearchDialog(editFrame,
				AddFrame.NAME_FILE_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(currentPath + "/testfiles"));

		// issue in MAC implementation s.
		// https://github.com/joel-costigliola/assertj-swing/issues/191
		if (System.getProperty("os.name").equals("Mac OS X")) {
			return;
		}
		fileChooser.selectFile(new File(currentPath
				+ "/testfiles/Hello World.rtf"));
		fileChooser.approve();

		editFrame.textBox(EditFrame.NAME_FILE_TEXT_FIELD).requireText(
				currentPath + File.separator + "testfiles" + File.separator
						+ "Hello World.rtf");
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
