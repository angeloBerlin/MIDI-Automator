package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.guiautomator.GUIAutomation;
import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.GUIAutomationTable;

/**
 * Loads the midi device when it was chosen in the ComboBox.
 * 
 * @author aguelle
 * 
 */
@Component
public class TriggerComboBoxListener implements ActionListener {

	@Autowired
	private MidiService midiService;

	private JComboBox<?> comboBox;

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JComboBox) {
			comboBox = (JComboBox<?>) e.getSource();
		}

		String functionKey = MidiAutomatorProperties.KEY_MIDI_IN_AUTOMATION_TRIGGER_DEVICE
				+ "_" + getSelectedAutomationIndex();
		String trigger = (String) comboBox.getSelectedItem();

		if (trigger.contains(GUIAutomation.TRIGGER_MIDI)) {
			midiService.loadMidiDeviceByFunctionKey(functionKey,
					trigger.replace(GUIAutomation.TRIGGER_MIDI, ""));
		} else {
			midiService.loadMidiDeviceByFunctionKey(functionKey,
					MidiAutomatorProperties.VALUE_NULL);
		}
	}

	/**
	 * Gets the automation index from the combo box name.
	 * 
	 * @return The automation index, -1 if there is no focus
	 */
	private int getSelectedAutomationIndex() {

		if (comboBox.getName() != null) {
			String automationIndex = comboBox.getName().replace(
					GUIAutomationTable.NAME_COMBOBOX_TRIGGER_EDITOR + "_", "");
			return Integer.parseInt(automationIndex);
		}

		return -1;
	}
}
