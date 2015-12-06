package com.midi_automator.tests;

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

public class AddFileFunctionalITCase extends GUITestCase {

	@Test
	public void newFileShouldBeAdded() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		addFile("Hello World", currentPath + "/testfiles/Hello World.rtf");

		String firstFileListEntry = getFileList().contents()[0];
		assertEquals("1 Hello World", firstFileListEntry);

		openEntryByDoubleClick(0);
		checkIfEntryWasOpened("Hello World");
	}

	@Test
	public void fileChooserOfAddDialogShouldChooseHelloWorldRtf() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		FrameFixture addFrame = openAddDialog();
		JFileChooserFixture fileChooser = openSearchDialog(addFrame);

		fileChooser.setCurrentDirectory(new File(currentPath + "/testfiles"));
		fileChooser.selectFile(new File(currentPath
				+ "/testfiles/Hello World.rtf"));
		fileChooser.approve();

		addFrame.textBox(AddFrame.NAME_FILE_TEXT_FIELD).requireText(
				currentPath + "/testfiles/Hello World.rtf");
	}

	@Test
	public void addingFileShouldBeCanceled() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
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
		startApplication();

		FrameFixture addFrame = openAddDialog();
		GUIAutomations.saveDialog(addFrame);

		assertEquals(getFileList().contents().length, 0);
	}

	@Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		startApplication();

		addFile("Hello World 129", currentPath + "/testfiles/Hello World.rtf");

		checkInfoText(String.format(Messages.MSG_FILE_LIST_IS_FULL,
				"Hello World 129"));
	}

	@Test
	public void everyNewEntryShouldHaveAUniqueMidiSendingSignature() {

		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
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
