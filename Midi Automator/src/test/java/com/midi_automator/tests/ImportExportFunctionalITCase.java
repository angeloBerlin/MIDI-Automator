package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.assertj.swing.fixture.JFileChooserFixture;
import org.fest.util.Files;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.FileUtils;

public class ImportExportFunctionalITCase extends GUITestCase {

	@Test
	public void midautoFileShouldBeExported() {

		String testFilePath = "testfiles";
		String testFileName = "ExportTest.midauto";

		MockUpUtils.setMockupPropertiesFile("mockups/RemoteINBus_1.properties");
		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12_empty.mido");
		startApplication();

		// export to file
		JFileChooserFixture fileChooser = openExportDialog();
		fileChooser.setCurrentDirectory(new File(testFilePath));
		fileChooser.fileNameTextBox().enterText("ExportTest");
		fileChooser.approve();

		try {

			// unzip exported file
			File midautoFile = new File(testFilePath + "/" + testFileName);
			FileUtils.unzipFile(new ZipFile(midautoFile), testFilePath);

			// compare file contents
			File midoFile = new File(testFilePath + "/file_list.mido");
			boolean midoCorrect = org.apache.commons.io.FileUtils
					.contentEquals(midoFile, new File(
							"mockups/Hello_World_12_empty.mido"));

			File propertiesFile = new File(testFilePath
					+ "/midiautomator.properties");
			boolean propertiesCorrect = org.apache.commons.io.FileUtils
					.contentEquals(propertiesFile, new File(
							"mockups/RemoteINBus_1.properties"));

			// delete exported files
			Files.delete(midoFile);
			Files.delete(propertiesFile);
			Files.delete(midautoFile);

			// test if contents are equal
			assertTrue(midoCorrect);
			assertTrue(propertiesCorrect);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void midautoFileShouldBeImported() {

		String testFilePath = "testfiles";
		String testFileName = "ImportTest.midauto";

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		// import midauto file
		JFileChooserFixture fileChooser = openImportDialog();
		fileChooser.setCurrentDirectory(new File(testFilePath));

		// Swing Look&Feel problem, s.
		// http://stackoverflow.com/questions/33945713/setselectedfilefile-file-does-not-present-selection-in-gui-of-jfilechooser/33948120#33948120
		// so we just check visibility of the dialog
		fileChooser.selectFile(new File(testFilePath + "/" + testFileName));
		fileChooser.requireVisible();
		fileChooser.cancel();

	}
}
