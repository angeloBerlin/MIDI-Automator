package com.midi.automator.tests;

import org.junit.After;
import org.junit.Before;

import com.midi.automator.tests.utils.MockUpUtils;
import com.midi.automator.tests.utils.SikuliAutomation;

public class GUITest extends SikuliAutomation {

	@Before
	public void setEmptyMockuos() {
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
	}

	@After
	public void deleteAllHelloWorldEntries() {

		setMinSimilarity(DEFAULT_SIMILARITY);
		MockUpUtils.recoverMidoBackup();
		MockUpUtils.recoverPropertiesBackup();
	}
}
