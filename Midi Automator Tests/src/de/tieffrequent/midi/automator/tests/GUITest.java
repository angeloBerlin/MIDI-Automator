package de.tieffrequent.midi.automator.tests;

import org.junit.After;

import de.tieffrequent.midi.automator.tests.utils.MockUpUtils;
import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

public class GUITest extends SikuliAutomation {

	@After
	public void deleteAllHelloWorldEntries() {

		MockUpUtils.recoverMidoBackup();
		MockUpUtils.recoverPropertiesBackup();
	}
}
