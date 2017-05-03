package com.midi_automator.tests.FunctionalTests;

import static com.midi_automator.tests.utils.GUIAutomations.*;
import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComboBox;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.utils.MidiUtils;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.menus.KeyLearnPopupMenu;

public class KeyAutomationFunctionalITCase extends FunctionalBaseCase {

	private String deviceName;
	private String propertiesMidiTriggerLeftClickAutomation;
	private String propertiesAlwaysCancelAutomation;

	private int messageType = ShortMessage.CONTROL_CHANGE;
	private int channel = 1;
	private int controlNo = 109;
	private int value = 127;

	private String focusedProgram;
	private int modifierCode;
	private int keyCode;
	private String[] cmd;

	public KeyAutomationFunctionalITCase() {

		if (System.getProperty("os.name").equals("Mac OS X")) {
			deviceName = "Bus 1";
			propertiesMidiTriggerLeftClickAutomation = "automation1_midiTrigger_Mac.properties";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Mac.properties";
			focusedProgram = "/Applications/TextEdit.app";
			cmd = new String[2];
			cmd[0] = "open";
			cmd[1] = focusedProgram;

			// CMD + q
			modifierCode = KeyEvent.VK_META;
			keyCode = KeyEvent.VK_Q;
		}

		if (System.getProperty("os.name").contains("Windows")) {
			deviceName = "LoopBe Internal MIDI";
			propertiesMidiTriggerLeftClickAutomation = "automation1_midiTrigger_Windows.properties";
			propertiesAlwaysCancelAutomation = "automation_cancel_always_left_Windows"
					+ ".properties";
			focusedProgram = "notepad.exe";
			cmd = new String[1];
			cmd[0] = focusedProgram;

			// ALT + F4
			modifierCode = KeyEvent.VK_ALT;
			keyCode = KeyEvent.VK_F4;
		}
	}

	@Test
	public void textEditorShouldBeFocusedAndClosedByShortcut() {

		try {
			MockUpUtils.setMockupPropertiesFile("mockups/"
					+ propertiesMidiTriggerLeftClickAutomation);
			MockUpUtils.setMockupMidoFile("mockups/empty.mido");

			// open text editor
			Runtime.getRuntime().exec(cmd);

			// start MIDI Automator
			startApplication();

			// set focus to text editor
			DialogFixture preferencesDialog = openPreferences();
			setFocusedProgram(focusedProgram, 0, preferencesDialog);
			saveDialog(preferencesDialog);
			Thread.sleep(1000);

			// send midi trigger to focus text editor
			MidiUtils.sendMidiMessage(deviceName, messageType, channel,
					controlNo, value);
			Thread.sleep(2000);

			// close text editor by key shortcut
			Robot robot = new Robot();
			robot.keyPress(modifierCode);
			robot.keyPress(keyCode);
			robot.keyRelease(modifierCode);
			robot.keyRelease(keyCode);

			// check if text editor is closed
			preferencesDialog = openPreferences();
			JComboBox<?> comboBox = getFocusProgramComboBox(0,
					preferencesDialog);

			for (int i = 0; i < comboBox.getItemCount(); i++) {
				String item = (String) comboBox.getItemAt(i);

				if (item.contains(focusedProgram)) {
					fail(focusedProgram + " was not closed.");
				}
			}

		} catch (InterruptedException | InvalidMidiDataException
				| MidiUnavailableException | AWTException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void preferencesShouldBeOpenedOnMidiByKeyAutomation() {

		if (!System.getProperty("os.name").equals("Mac OS X")) {
			try {
				MockUpUtils.setMockupPropertiesFile("mockups/"
						+ propertiesMidiTriggerLeftClickAutomation);
				MockUpUtils.setMockupMidoFile("mockups/empty.mido");
				startApplication();

				// set type to send keys
				DialogFixture preferencesDialog = openPreferences();
				setAutomationType(GUIAutomation.TYPE_SENDKEY, 0,
						preferencesDialog);

				// key learn ALT + P
				keyLearnAutomation(0, preferencesDialog);
				pressAndReleaseKeysOnGUIAutomationTable(preferencesDialog,
						KeyEvent.VK_ALT, KeyEvent.VK_P);
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
		int column = table.columnIndexFor(GUIAutomationTable.COLNAME_KEYS);
		table.requireCellValue(TableCell.row(0).column(column), "");
	}

	// @Test
	// Blocked due to
	// https://github.com/joel-costigliola/assertj-swing/issues/169"
	public void keyLearnShouldBeCanceledWithLearnedKeys() {

		MockUpUtils.setMockupPropertiesFile("mockups/"
				+ propertiesAlwaysCancelAutomation);
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		// set type to send keys
		DialogFixture preferencesDialog = openPreferences();
		setAutomationType(GUIAutomation.TYPE_SENDKEY, 0, preferencesDialog);

		// cancel key learn after invoking
		keyLearnAutomation(0, preferencesDialog);
		pressAndReleaseKeysOnGUIAutomationTable(preferencesDialog, 19, 81);
		try {
			cancelKeyLearnAutomation(0, preferencesDialog);
		} catch (IllegalStateException e) {
			fail("JMenuItem with the name "
					+ KeyLearnPopupMenu.NAME_MENU_ITEM_KEYS_CANCEL
					+ " is not enabled or not shown.");
		}

		// check if learned keys are revoked
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table.columnIndexFor(GUIAutomationTable.COLNAME_KEYS);
		int[] keyCodes = { 18, 80 };
		table.requireCellValue(TableCell.row(0).column(column),
				GUIAutomationTable.keyCodesToString(keyCodes));

	}

	// @Test
	// Blocked due to
	// https://github.com/joel-costigliola/assertj-swing/issues/169"
	public void keyLearnShouldBeCanceled() {

		MockUpUtils
				.setMockupPropertiesFile("mockups/automation1_empty.properties");
		MockUpUtils.setMockupMidoFile("mockups/empty.mido");
		startApplication();

		// set type to send keys
		DialogFixture preferencesDialog = openPreferences();
		setAutomationType(GUIAutomation.TYPE_SENDKEY, 0, preferencesDialog);

		// cancel key learn after invoking
		keyLearnAutomation(0, preferencesDialog);
		cancelKeyLearnAutomation(0, preferencesDialog);

		// check if learned keys are revoked
		JTableFixture table = getGUIAutomationTable(preferencesDialog);
		int column = table.columnIndexFor(GUIAutomationTable.COLNAME_KEYS);
		int[] keyCodes = {};
		table.requireCellValue(TableCell.row(0).column(column),
				GUIAutomationTable.keyCodesToString(keyCodes));

	}
}
