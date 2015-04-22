package com.midi_automator.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sikuli.script.Region;

import com.midi_automator.tests.utils.SikuliAutomation;

@RunWith(Suite.class)
@SuiteClasses({ TestOpenMidiAutomator.class, TestAddFile.class,
		TestOpenFile.class, TestEditFile.class, TestDeleteFile.class,
		TestMoveUpDownFile.class, TestSwitchButtons.class,
		TestMidiLearnSwitchButtons.class, TestMidiLearnList.class,
		TestMidiRemoteOpen.class, TestMidiRemoteOut.class,
		TestMidiMetronom.class, TestMidiNotifierOut.class,
		TestMouseAutomation.class, TestCloseMidiAutomator.class })
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
