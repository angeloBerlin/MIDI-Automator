package com.midi_automator.tests;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddFileFunctionalITCase.class, //
		OpenItemFileFunctionalITCase.class, //
		EditFileFunctionalITCase.class, //
		DeleteFileFunctionalITCase.class,//
		MoveEntriesFunctionalITCase.class,//
		SwitchButtonsFunctionalITCase.class, //
		MidiLearnSwitchButtonsFunctionalITCase.class, //
		MidiLearnListFunctionalITCase.class, //
		MidiRemoteOpenFunctionalITCase.class, //
		MidiRemoteOutFunctionalITCase.class, //
		MidiMetronomFunctionalITCase.class, //
		MidiNotifierOutFunctionalITCase.class, //
		MidiSendSwitchItemFunctionalITCase.class, //
		GUIAutomationFunctionalITCase.class, //
		ImportExportFunctionalITCase.class //
})
public class AllFunctionalIntegrationTests {

	@AfterClass
	public static void tearDown() {
		System.out.println("End of Tests!");
	}

}
