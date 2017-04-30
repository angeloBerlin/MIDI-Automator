package com.midi_automator.view.tray.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.model.MidiAutomatorProperties;
import com.midi_automator.presenter.services.MidiLearnService;
import com.midi_automator.presenter.services.MidiService;
import com.midi_automator.view.tray.Tray;
import com.midi_automator.view.tray.menus.TrayPopupMenu;
import com.midi_automator.view.windows.MainFrame.MainFrame;

@Component
public class TrayMouseListener extends MouseAdapter {

	@Autowired
	private TrayPopupMenu popupMenu;
	@Autowired
	private MidiService midiService;
	@Autowired
	private MidiLearnService midiLearnService;
	@Autowired
	private MainFrame mainFrame;

	@Override
	public void mouseClicked(MouseEvent e) {

		if (!popupMenu.isVisible()) {
			configureMidiLearnItem();
			configureMidiUnlearnItem();
			popupMenu.open(e.getX(), e.getY());
		} else {
			popupMenu.close();
		}
	}

	/**
	 * Sets the MIDI learn menu item enabled or disabled depending if a MIDI IN
	 * remote device exists.
	 */
	private void configureMidiLearnItem() {

		popupMenu.getMidiLearnMenuItem().setEnabled(false);

		String midiInRemoteDeviceName = midiService
				.getMidiDeviceName(MidiAutomatorProperties.KEY_MIDI_IN_REMOTE_DEVICE);

		if (midiInRemoteDeviceName != null
				&& !midiInRemoteDeviceName
						.equals(MidiAutomatorProperties.VALUE_NULL)) {
			popupMenu.getMidiLearnMenuItem().setEnabled(true);
		}
	}

	/**
	 * Sets the MIDI unlearn menu item enabled or disabled depending if a MIDI
	 * signature was learned for the component.
	 */
	private void configureMidiUnlearnItem() {

		popupMenu.getMidiUnlearnMenuItem().setEnabled(false);

		if (midiLearnService.isMidiLearned(Tray.NAME)) {
			popupMenu.getMidiUnlearnMenuItem().setEnabled(true);
		}
	}
}
