package com.midi_automator.tests.functional;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;

public class PreferencesFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;

	public PreferencesFunctionalITCase() {
		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
		}
	}

	@Test
	public void prefernecesShouldBeSavedOnEnterSaveButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// set midi in device
		DialogFixture preferencesDialog = openPreferences();
		setMidiInRemoteDevice(deviceName, preferencesDialog);
		saveDialogByEnter(preferencesDialog);

		// check midi device
		preferencesDialog = openPreferences();
		preferencesDialog.comboBox(
				PreferencesDialog.NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX)
				.requireSelection(deviceName);
	}

	@Test
	public void prefernecesShouldBeCanceledOnEnterCancelButton() {

		MockUpUtils.setMockupMidoFile("mockups/Hello_World_12.mido");
		MockUpUtils.setMockupPropertiesFile("mockups/empty.properties");
		startApplication();

		// set midi in device
		DialogFixture preferencesDialog = openPreferences();
		setMidiInRemoteDevice(deviceName, preferencesDialog);
		cancelDialogByEnter(preferencesDialog);

		// check midi device
		preferencesDialog = openPreferences();
		preferencesDialog.comboBox(
				PreferencesDialog.NAME_MIDI_IN_REMOTE_DEVICE_COMBO_BOX)
				.requireSelection(MidiAutomatorProperties.VALUE_NULL);
	}
}
