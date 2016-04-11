package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import java.awt.event.KeyEvent;

import org.assertj.swing.finder.WindowFinder;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.frames.PreferencesFrame;

public class MnemonicsFunctionalITCase extends FunctionalBaseCase {

	public MnemonicsFunctionalITCase() {

	}

	@Test
	public void importDialogShallBeOpenedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressKeyOnMainFrame(KeyEvent.VK_I);

		window.fileChooser(MainFrame.NAME_MIDI_IMPORT_FILECHOOSER)
				.requireVisible();
	}

	@Test
	public void exportDialogShallBeOpenedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_X);

		window.fileChooser(MainFrame.NAME_MIDI_EXPORT_FILECHOOSER)
				.requireVisible();
	}

	@Test
	public void preferencesDialogShallBeOpenedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_P);

		WindowFinder.findFrame(PreferencesFrame.NAME).using(robot)
				.requireVisible();
	}

	// @Test
	public void midiAutomatorShallBeClosedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_E);
	}
}
