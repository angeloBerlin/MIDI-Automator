package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.io.File;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.junit.Test;

import com.midi_automator.presenter.Messages;
import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.frames.AddFrame;

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
			programDirectory = "C:/Program Files/Windows NT/Accessories";
			programPath = programDirectory + "wordpad.exe";
			programScreenshot = "Wordpad.png";
		}
	}

	@Test
	public void newFileShouldBeAdded() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		addFile("Hello World", currentPath + "/testfiles/Hello World.rtf",
				programPath);

		String firstFileListEntry = getFileList().contents()[0];
		assertEquals("1 Hello World", firstFileListEntry);

		openEntryByDoubleClick(0);

		checkIfOpenEntryIsDisplayed("Hello World");
		sikulix.checkIfProgramOpened(programScreenshot);
		sikulix.checkIfFileOpened("Hello_World_RTF.png");
	}

	@Test
	public void fileChooserOfAddDialogShouldChooseHelloWorldRtf() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addFrame,
				AddFrame.NAME_FILE_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(currentPath + "/testfiles"));
		String rtfPath = currentPath + File.separator + "testfiles"
				+ File.separator + "Hello World.rtf";
		fileChooser.selectFile(new File(rtfPath));
		fileChooser.approve();

		addFrame.textBox(AddFrame.NAME_FILE_TEXT_FIELD).requireText(rtfPath);
	}

	// @Test
	public void programChooserOfAddDialogShouldChooseSpecificProgram() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addFrame,
				AddFrame.NAME_PROGRAM_SEARCH_BUTTON);

		fileChooser.setCurrentDirectory(new File(programDirectory));

		// TODO: JFileChooserFixture can only select files
		fileChooser.selectFile(new File(programPath));

		fileChooser.approve();

		addFrame.textBox(AddFrame.NAME_PROGRAM_TEXT_FIELD).requireText(
				programPath);
	}

	@Test
	public void addingFileShouldBeCanceled() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		addFrame.textBox(AddFrame.NAME_NAME_TEXT_FIELD).setText("x");
		addFrame.textBox(AddFrame.NAME_FILE_TEXT_FIELD).setText("y");
		cancelDialog(addFrame);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingEmptyFileNameShouldNotBePossible() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		GUIAutomations.saveDialog(addFrame);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		addFile("Hello World 129", currentPath + "/testfiles/Hello World.rtf",
				"");

		checkInfoText(String.format(Messages.MSG_FILE_LIST_IS_FULL,
				"Hello World 129"));
	}

	@Test
	public void everyNewEntryShouldHaveAUniqueMidiSendingSignature() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		String sendingSignature = addFrame.label(
				AddFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 1 value: 127");

		addFrame.textBox(AddFrame.NAME_NAME_TEXT_FIELD)
				.setText("Hello World 1");
		saveDialog(addFrame);
		addFrame = openAddDialog();
		sendingSignature = addFrame.label(
				AddFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 2 value: 127");

		addFrame.textBox(AddFrame.NAME_NAME_TEXT_FIELD)
				.setText("Hello World 2");
		saveDialog(addFrame);

		addFrame = openAddDialog();
		sendingSignature = addFrame.label(
				AddFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();

		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 3 value: 127");

		cancelDialog(addFrame);

		deleteEntry(1);
		addFrame = openAddDialog();
		sendingSignature = addFrame.label(
				AddFrame.NAME_MIDI_SENDING_SIGNATURE_VALUE_LABEL).text();
		assertEquals(sendingSignature,
				"channel 16: CONTROL CHANGE 2 value: 127");
	}
}
