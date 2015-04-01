package com.midi.automator.tests;

import org.junit.After;

import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class GUITest extends SikuliAutomation {

	@After
	public void deleteAllHelloWorldEntries() {

		MockUpUtils.recoverMidoBackup();
		MockUpUtils.recoverPropertiesBackup();
	}
}
