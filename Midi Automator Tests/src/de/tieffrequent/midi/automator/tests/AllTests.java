package de.tieffrequent.midi.automator.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestOpenMidiAutomator.class, TestCancelAddFile.class,
		TestAddFile.class, TestOpenFile.class })
public class AllTests {

	@BeforeClass
	public static void setUp() {

	}

	@AfterClass
	public static void tearDown() {
	}

}
