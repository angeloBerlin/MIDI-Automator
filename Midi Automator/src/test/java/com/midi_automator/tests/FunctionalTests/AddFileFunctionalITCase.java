package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.io.File;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.junit.Test;

import com.midi_automator.presenter.Messages;
import com.midi_automator.presenter.services.FileListService;
import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.frames.AddDialog;

public class AddFileFunctionalITCase extends FunctionalBaseCase {

	private String programPath;
	private String programScreenshot;
	private String programDirectory;

	public AddFileFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			programDirectory = "/Applications/Microsoft Office 2011/";
			programPath = programDirectory + "Microsoft Word.app";
			programScreenshot = "Word.png";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			programDirectory = "C:/Windows/System32/";
			programPath = "C:\\Windows\\System32\\" + "notepad.exe";
			programScreenshot = "Notepad.png";
		}
	}

	@Test
	public void fileChooserShouldRememberLastDirectory() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_FILE_SEARCH_BUTTON);

		// choose a file
		fileChooser.setCurrentDirectory(new File(currentPath + File.separator
				+ "testfiles"));
		String rtfPath = currentPath + File.separator + "testfiles"
				+ File.separator + "Hello World.rtf";
		File rtfFile = new File(rtfPath);
		fileChooser.selectFile(rtfFile);
		fileChooser.approve();

		saveDialog(addDialog);

		// re-select file
		addDialog = openAddDialog();
		fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_FILE_SEARCH_BUTTON);

		fileChooser.selectFile(rtfFile);
		fileChooser.approve();

		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).requireText(rtfPath);
	}

	@Test
	public void programChooserShouldRememberLastDirectory() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_PROGRAM_SEARCH_BUTTON);

		// choose a file
		fileChooser.setCurrentDirectory(new File(currentPath + File.separator
				+ "testfiles"));
		String rtfPath = currentPath + File.separator + "testfiles"
				+ File.separator + "Hello World.rtf";
		File rtfFile = new File(rtfPath);
		fileChooser.selectFile(rtfFile);
		fileChooser.approve();

		saveDialog(addDialog);

		// re-select file
		addDialog = openAddDialog();
		fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_PROGRAM_SEARCH_BUTTON);

		fileChooser.selectFile(rtfFile);
		fileChooser.approve();

		addDialog.textBox(AddDialog.NAME_PROGRAM_TEXT_FIELD).requireText(
				rtfPath);
	}

	@Test
	public void newFileShouldBeAdded() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		saveDialog(addFile("Hello World", currentPath
				+ "/testfiles/Hello World.rtf", programPath));

		String firstFileListEntry = getFileList().contents()[0];
		assertEquals("1 Hello World", firstFileListEntry);

		openEntryByDoubleClick(0);
		try {
			Thread.sleep(FileListService.FAST_SWITCHING_TIMESLOT * 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		checkIfOpenEntryIsDisplayed("Hello World");
		sikulix.checkIfProgramOpened(programScreenshot);
		sikulix.checkIfFileOpened("Hello_World_RTF.png");
	}

	@Test
	public void newFileShouldBeAddedOnEnterSaveButton() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		saveDialogByEnter(addFile("Hello World", currentPath
				+ "/testfiles/Hello World.rtf", programPath));

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String firstFileListEntry = getFileList().contents()[0];
		assertEquals("1 Hello World", firstFileListEntry);
	}

	@Test
	public void fileChooserOfAddDialogShouldChooseHelloWorldRtf() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_FILE_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(currentPath + File.separator
				+ "testfiles"));
		String rtfPath = currentPath + File.separator + "testfiles"
				+ File.separator + "Hello World.rtf";
		fileChooser.selectFile(new File(rtfPath));
		fileChooser.approve();

		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).requireText(rtfPath);
	}

	@Test
	public void fileChooserOfAddDialogShouldChooseHelloWorldRtfByKeyboard() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialogOnEnter(addDialog,
				AddDialog.NAME_FILE_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(currentPath + File.separator
				+ "testfiles"));
		String rtfPath = currentPath + File.separator + "testfiles"
				+ File.separator + "Hello World 1.rtf";

		// issue in MAC implementation s.
		// https://github.com/joel-costigliola/assertj-swing/issues/191
		if (System.getProperty("os.name").equals("Mac OS X")) {
			return;
		}

		fileChooser.selectFile(new File(rtfPath));
		fileChooser.approve();
		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).requireText(rtfPath);
	}

	@Test
	public void programChooserOfAddDialogShouldChooseSpecificProgram() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addDialog,
				AddDialog.NAME_PROGRAM_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(programDirectory));

		// issue in MAC implementation s.
		// https://github.com/joel-costigliola/assertj-swing/issues/192
		if (System.getProperty("os.name").contains("Mac")) {
			return;
		}
		fileChooser.selectFile(new File(programPath));

		fileChooser.approve();

		addDialog.textBox(AddDialog.NAME_PROGRAM_TEXT_FIELD).requireText(
				programPath);
	}

	@Test
	public void addingFileShouldBeCanceled() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		addDialog.textBox(AddDialog.NAME_NAME_TEXT_FIELD).setText("x");
		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialog(addDialog);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingFileShouldBeCanceledByEnterCancelButton() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		addDialog.textBox(AddDialog.NAME_NAME_TEXT_FIELD).setText("x");
		addDialog.textBox(AddDialog.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialogByEnter(addDialog);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingEmptyFileNameShouldNotBePossible() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		GUIAutomations.saveDialog(addDialog);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		saveDialog(addFile("Hello World 129", currentPath
				+ "/testfiles/Hello World.rtf", ""));

		checkInfoText(String.format(Messages.MSG_FILE_LIST_IS_FULL,
				"Hello World 129"));
	}

	@Test
	public void everyNewEntryShouldHaveAUniqueMidiSendingSignature() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		DialogFixture addDialog = openAddDialog();
		String sendingSignature = addDialog.label(
				AddDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 1 value: 127");

		addDialog.textBox(AddDialog.NAME_NAME_TEXT_FIELD).setText(
				"Hello World 1");
		saveDialog(addDialog);
		addDialog = openAddDialog();
		sendingSignature = addDialog.label(
				AddDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 2 value: 127");

		addDialog.textBox(AddDialog.NAME_NAME_TEXT_FIELD).setText(
				"Hello World 2");
		saveDialog(addDialog);

		addDialog = openAddDialog();
		sendingSignature = addDialog.label(
				AddDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 3 value: 127");

		cancelDialog(addDialog);

		deleteEntry(1);
		addDialog = openAddDialog();
		sendingSignature = addDialog.label(
				AddDialog.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();
		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 2 value: 127");
	}
}
