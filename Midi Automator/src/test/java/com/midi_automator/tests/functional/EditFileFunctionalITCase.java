package com.midi_automator.tests.functional;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.io.File;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JMenuItemFixture;
import org.assertj.swing.fixture.JPopupMenuFixture;
import org.junit.Assume;
import org.junit.Test;

import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.windows.AddDialog.AddDialog;
import com.midi_automator.view.windows.EditDialog.EditDialog;
import com.midi_automator.view.windows.MainFrame.menus.MainFramePopupMenu;

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

		DialogFixture editDialog = openEditDialog(0);
		editDialog.textBox(EditDialog.NAME_NAME_TEXT_FIELD).setText("x");
		editDialog.textBox(EditDialog.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialog(editDialog);

		assertEquals(getFileList().contents()[0], "1 Hello World");
	}

	@Test
	public void editingFileShouldBeCanceledByEnterCancelButton() {

		// issue in sending keys on mac
		Assume.assumeFalse(System.getProperty("os.name").equals("Mac OS X"));

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture editDialog = openEditDialog(0);
		editDialog.textBox(EditDialog.NAME_NAME_TEXT_FIELD).setText("x");
		editDialog.textBox(EditDialog.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialogByEnter(editDialog);

		assertEquals(getFileList().contents()[0], "1 Hello World");
	}

	@Test
	public void helloWorldShouldBeEdited() {

		try {
			MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
			MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
			startApplication();

			DialogFixture editDialog = openEditDialog(0);
			editDialog.textBox(EditDialog.NAME_NAME_TEXT_FIELD).setText(
					"Hello World Edit");
			editDialog.textBox(EditDialog.NAME_FILE_TEXT_FIELD).setText(
					currentPath + "/testfiles/Hello World edit.rtf");
			editDialog.textBox(EditDialog.NAME_PROGRAM_TEXT_FIELD).setText(
					editedProgramPath);
			saveDialog(editDialog);

			assertEquals(getFileList().contents()[0], "1 Hello World Edit");

			openEntryByDoubleClick(0);
			Thread.sleep(ItemListService.FAST_SWITCHING_TIMESLOT * 3);

			checkIfOpenEntryIsDisplayed("Hello World Edit");
			sikulix.checkIfProgramOpened(editedProgramScreenshot);
			sikulix.checkIfFileOpened("Hello_World_Edit_RTF.png");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void helloWorldShouldBeEditedOnEnterSaveButton() {

		// issue in sending keys on mac
		Assume.assumeFalse(System.getProperty("os.name").equals("Mac OS X"));

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture editDialog = openEditDialog(0);
		editDialog.textBox(EditDialog.NAME_NAME_TEXT_FIELD).setText(
				"Hello World Edit");
		editDialog.textBox(EditDialog.NAME_FILE_TEXT_FIELD).setText(
				currentPath + "/testfiles/Hello World edit.rtf");
		editDialog.textBox(EditDialog.NAME_PROGRAM_TEXT_FIELD).setText(
				editedProgramPath);
		saveDialogByEnter(editDialog);

		assertEquals(getFileList().contents()[0], "1 Hello World Edit");
	}

	@Test
	public void fileChooserOfEditDialogShouldBeOpened() {

		MockUpUtils.setMockupMidoFile("mockups/" + helloWorldMido);
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture editDialog = openEditDialog(0);
		JFileChooserFixture fileChooser = openSearchDialog(editDialog,
				AddDialog.NAME_FILE_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(currentPath + "/testfiles"));

		// issue in MAC implementation s.
		// https://github.com/joel-costigliola/assertj-swing/issues/191
		if (System.getProperty("os.name").equals("Mac OS X")) {
			return;
		}
		fileChooser.selectFile(new File(currentPath
				+ "/testfiles/Hello World.rtf"));
		fileChooser.approve();

		editDialog.textBox(EditDialog.NAME_FILE_TEXT_FIELD).requireText(
				currentPath + File.separator + "testfiles" + File.separator
						+ "Hello World.rtf");
	}

	@Test
	public void sendingMidiSignatureShouldBeShown() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture editDialog = openEditDialog(0);
		editDialog.label(EditDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL)
				.requireText("channel 16: CONTROL CHANGE 1 value: 127");

		cancelDialog(editDialog);

		editDialog = openEditDialog(1);
		editDialog.label(EditDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL)
				.requireText("channel 16: CONTROL CHANGE 2 value: 127");
	}
}
