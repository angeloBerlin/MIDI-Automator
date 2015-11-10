package com.midi_automator.tests;

import org.junit.Test;

import com.midi_automator.tests.utils.AssertJGUIAutomations;

public class AddFileFunctionalITCase extends GUITestCase {

	@Test
	public void newFileShouldBeAdded() {

		AssertJGUIAutomations.addFile("Hello World", currentPath
				+ "/testfiles/Hello World.rtf");
		// try {
		// GUIAutomations.addFile("Hello World", currentPath
		// + "/testfiles/Hello World.rtf");
		//
		// // search new entry
		// GUIAutomations.checkResult("Hello_World_entry.png");
		//
		// // check if new entry was saved
		// GUIAutomations.closeMidiAutomator();
		// GUIAutomations.openMidiAutomator();
		// GUIAutomations.checkResult("Hello_World_entry.png");
		//
		// } catch (FindFailed | IOException e) {
		// fail(e.toString());
		// } finally {
		// try {
		// GUIAutomations.closeMidiAutomator();
		// } catch (FindFailed e) {
		// e.printStackTrace();
		// }
		// }
	}

	@Test
	public void fileChooserOfAddDialogShouldBeOpened() {

	}

	@Test
	public void addingFileShouldBeCanceled() {

	}

	@Test
	public void addingEmptyFileNameShouldNotBePossible() {

	}

	@Test
	public void addingMoreFilesThan128ShouldBeImpossible() {

	}
}
