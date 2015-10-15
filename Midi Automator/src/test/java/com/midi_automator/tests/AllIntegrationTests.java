package com.midi_automator.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.sikuli.script.Region;

import com.midi_automator.tests.utils.SikuliAutomation;

@RunWith(Suite.class)
@SuiteClasses({ OpenMidiAutomatorITCase.class, AddFileITCase.class,
		OpenItemFileITCase.class, EditFileITCase.class, DeleteFileITCase.class,
		MoveUpDownFileITCase.class, SwitchButtonsITCase.class,
		MidiLearnSwitchButtonsITCase.class, MidiLearnListITCase.class,
		MidiRemoteOpenITCase.class, MidiRemoteOutITCase.class,
		MidiMetronomITCase.class, MidiNotifierOutITCase.class,
		MidiListEntrySendITCase.class, GUIAutomationITCase.class,
		ImportExportITCase.class, CloseMidiAutomatorITCase.class })
public class AllIntegrationTests extends SikuliAutomation {

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
		AllIntegrationTests.programRegion = programRegion;
	}

}
