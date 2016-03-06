package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.clickPrevFile;
import static com.midi_automator.tests.utils.GUIAutomations.openEntryByDoubleClick;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;

public class FileListFunctionalITCase extends FunctionalBaseCase {

	@Test
	public void item2ShouldScrollToTopIfSelected() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(1);

		if (!sikulix.checkforStates("selected_Hello_World_2.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item128ShouldbeVisibleIfItem125IsSelected() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(124);

		if (!sikulix.checkforStates("selected_Hello_World_125.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item128ShouldbeVisibleIfDecreasedFromItem1ToItem125() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		clickPrevFile();
		clickPrevFile();
		clickPrevFile();
		clickPrevFile();

		if (!sikulix.checkforStates("selected_Hello_World_125.png")) {
			fail("Incorrect scrolling");
		}
	}

	@Test
	public void item2ShoulScrollToTopIfItem3WasDecreased() {

		MockUpUtils.setMockupMidoFile("mockups/128_Hello_World.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		openEntryByDoubleClick(2);
		clickPrevFile();

		if (!sikulix.checkforStates("selected_Hello_World_2.png")) {
			fail("Incorrect scrolling");
		}
	}
}
