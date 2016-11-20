package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import java.awt.event.KeyEvent;

import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.frames.MainFrame;
import com.midi_automator.view.frames.PreferencesDialog;

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

		JFileChooserFixture fileChooser = window.fileChooser(
				MainFrame.NAME_MIDI_IMPORT_FILECHOOSER).requireVisible();
		fileChooser.cancel();
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}

	@Test
	public void exportDialogShallBeOpenedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_X);

		JFileChooserFixture fileChooser = window.fileChooser(
				MainFrame.NAME_MIDI_EXPORT_FILECHOOSER).requireVisible();
		fileChooser.cancel();
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}

	@Test
	public void preferencesDialogShallBeOpenedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_P);

		FrameFixture preferncesFrame = WindowFinder
				.findFrame(PreferencesDialog.NAME).using(robot).requireVisible();
		preferncesFrame.close();
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}

	// @Test
	public void midiAutomatorShallBeClosedByMnemonic() {

		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		pressKeyOnMainFrame(KeyEvent.VK_ALT);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_E);
		pressAndReleaseKeysOnMainFrame(KeyEvent.VK_ALT);
	}
}
