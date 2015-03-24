package de.tieffrequent.midi.automator.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sikuli.script.Region;

import de.tieffrequent.midi.automator.tests.utils.SikuliAutomation;

@RunWith(Suite.class)
@SuiteClasses({ TestOpenMidiAutomator.class, TestAddFile.class,
		TestOpenFile.class, TestEditFile.class, TestDeleteFile.class,
		TestMoveUpFile.class, TestMoveDownFile.class, TestButtonNextFile.class,
		TestButtonPrevFile.class, TestMidiLearnNext.class,
		TestMidiLearnPrev.class, TestMidiLearnList.class,
		TestMidiRemoteOpen.class, TestCloseMidiAutomator.class })
public class AllTests extends SikuliAutomation {

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
