package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.automationconfiguration.GUIAutomationConfigurationTable;

public class KeyAutomationFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String propertiesMidiTriggerAutomation;
	private String propertiesAlwaysCancelAutomation;

	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	public KeyAutomationFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesMidiTriggerAutomation = "automation1_midiTrigger_Mac.properties";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Mac.properties";
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesMidiTriggerAutomation = "automation1_midiTrigger_Windows.properties";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
		}
	}

	@Test
	public void preferencesShouldBeOpenedOnMidi() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiTriggerAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");
			startApplication();

			// set type to send keys
			DialogFixture preferencesDialog = openPreferences();
			setAutomationType(GUIAutomation.TYPE_SENDKEY, 0, preferencesDialog);

			// key learn ALT + P
			keyLearnAutomation(0, preferencesDialog);
			pressAndReleaseKeysOnGUIAutomationTable(preferencesDialog, 18, 80);
			submitKeyLearnAutomation(0, preferencesDialog);
			saveDialog(preferencesDialog);

			// send midi trigger
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(1000);

			// check if preferences frame is visible
			preferencesDialog.requireVisible();

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void keysShouldBeUnlearned() {

		MockUpUtils.setMockupPropertiesFile("mockups/"
				+ propertiesAlwaysCancelAutomation);
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		// unlearn keys
		DialogFixture preferencesDialog = openPreferences();
		keyUnLearnAutomation(0, preferencesDialog);

		// check if keys were unlearned
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);
		table.requireCellValue(TableCell.row(0).column(column), "");
	}

	@Test
	public void keyLearnShouldBeCanceled() {

		MockUpUtils.setMockupPropertiesFile("mockups/"
				+ propertiesAlwaysCancelAutomation);
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		// set type to send keys
		DialogFixture preferencesDialog = openPreferences();
		setAutomationType(GUIAutomation.TYPE_SENDKEY, 0, preferencesDialog);

		// cancel key learn
		keyLearnAutomation(0, preferencesDialog);
		pressAndReleaseKeysOnGUIAutomationTable(preferencesDialog, 19, 81);
		cancelKeyLearnAutomation(0, preferencesDialog);

		// check if learned keys are revoked
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table
				.columnIndexFor(GUIAutomationConfigurationTable.COLNAME_KEYS);
		int[] keyCodes = { 18, 80 };
		table.requireCellValue(TableCell.row(0).column(column),
				GUIAutomationConfigurationTable.keyCodesToString(keyCodes));

	}
}
