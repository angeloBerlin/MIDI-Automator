package de.tieffrequent.midi.automator.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sikuli.script.Region;

@RunWith(Suite.class)
@SuiteClasses({ TestOpenMidiAutomator.class, TestCancelAddFile.class,
		TestAddFile.class, TestOpenFile.class, TestEditFile.class })
public class AllTests extends SikuliTest {

	private static Region programRegion;

	@BeforeClass
	public static void setUp() {

	}

	@AfterClass
	public static void tearDown() {
		System.out.println("End of Tests!");
	}

	public static Region getProgramRegion() {
		return programRegion;
	}

	public static void setProgramRegion(Region programRegion) {
		AllTests.programRegion = programRegion;
	}

}
