package com.midi_automator.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.tests.utils.SikuliAutomation;

public class GUITest extends SikuliAutomation {

	@Rule
	public TestName name = new TestName();

	@Before
	public void log() {
		System.out.println();
		System.out.println("Running Test: " + name.getMethodName());
		System.out
				.println("====================================================");
	}

	@Before
	public void setDefaults() {
		setMinSimilarity(DEFAULT_SIMILARITY);
	}

	@Before
	public void setEmptyMockups() {
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
	}

	@After
	public void undoMockups() {
		MockUpUtils.recoverMidoBackup();
		MockUpUtils.recoverPropertiesBackup();
	}
}
