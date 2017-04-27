package com.midi_automator.tests.IntegrationTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.services.ItemListService;
import com.midi_automator.presenter.services.GUIAutomationsService;
import com.midi_automator.presenter.services.ImportExportService;
import com.midi_automator.presenter.services.MidiService;

public class ImportFileIntegrationTest extends IntegrationTBase {

	@Autowired
	private ImportExportService importExportService;
	@Autowired
	private ItemListService fileListService;
	@Autowired
	private MidiService midiService;
	@Autowired
	private GUIAutomationsService guiAutomationsService;
	@Autowired
	private MidiAutomatorProperties properties;

	@Test
	public void midautoFileShouldBeImported() {

		String testFilePath = "testfiles";
		String testFileName = "ImportTest.midauto";

		importExportService.importMidautoFile(new File(testFilePath + "/"
				+ testFileName));

		// test file_list.mido
		assertEquals("Hello World 1", fileListService.getEntryNameByIndex(0));
		assertEquals("Hello World 2", fileListService.getEntryNameByIndex(1));
		assertEquals("channel 16: CONTROL CHANGE 1 value: 127",
				fileListService.getMidiFileListSendingSignature(0));
		assertEquals("channel 16: CONTROL CHANGE 2 value: 127",
				fileListService.getMidiFileListSendingSignature(1));

		// test midautomator.properties
		assertEquals(
				midiService
						.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE),
				"Bus 1");
	}

	@Test
	public void midautoFileShouldBeMigrated() {

		String testFilePath = "testfiles";
		String testFileName = "ImportTest.midauto";

		importExportService.importMidautoFile(new File(testFilePath + "/"
				+ testFileName));

		// test midautomator.properties
		assertEquals(guiAutomationsService.getMinSimilarity(), 0.7, 0.1f);
		assertNull(properties.get("GUI_AUTOMATION_MIN_SIMILARITY_0"));
		assertNull(properties.get("GUI_AUTOMATION_MIN_SIMILARITY_1"));
	}
}
