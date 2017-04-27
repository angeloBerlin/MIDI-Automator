package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import java.awt.event.KeyEvent;

import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.junit.Test;

import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.windows.ExportFileChooser.ExportFileChooser;
import com.midi_automator.view.windows.ImportFileChooser.ImportFileChooser;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;

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
				ImportFileChooser.NAME).requireVisible();
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
				ExportFileChooser.NAME).requireVisible();
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

		DialogFixture preferncesDialog = WindowFinder
				.findDialog(PreferencesDialog.NAME).using(robot)
				.requireVisible();
		preferncesDialog.close();
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
