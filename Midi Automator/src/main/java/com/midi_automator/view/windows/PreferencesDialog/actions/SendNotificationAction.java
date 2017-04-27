package com.midi_automator.view.windows.PreferencesDialog.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.MidiItemChangeNotificationService;
import com.midi_automator.view.windows.PreferencesDialog.PreferencesDialog;

/**
 * Sends a notification through the chosen midi device.
 * 
 * @author aguelle
 * 
 */
@Component
public class SendNotificationAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private PreferencesDialog preferencesDialog;
	@Autowired
	private MidiItemChangeNotificationService midiNotificationService;

	@Override
	public void actionPerformed(ActionEvent e) {

		String deviceName = preferencesDialog.getSwitchNotifierMidiDeviceName();
		midiNotificationService.sendItemChangeNotifier(deviceName);

	}
}
