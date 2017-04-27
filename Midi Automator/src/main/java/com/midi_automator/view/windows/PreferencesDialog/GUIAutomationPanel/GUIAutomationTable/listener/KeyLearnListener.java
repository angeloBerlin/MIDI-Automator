package com.midi_automator.view.windows.PreferencesDialog.GUIAutomationPanel.GUIAutomationTable.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.KeyLearnService;

/**
 * Learns a keyboard key or shortcut.
 * 
 * @author aguelle
 * 
 */
@Component
public class KeyLearnListener extends KeyAdapter {

	protected Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private KeyLearnService keyLearnService;

	@Override
	public void keyPressed(KeyEvent e) {

		// prevent any further OS listener to react on the key
		e.consume();

		if (keyLearnService.isKeyLearning()) {

			keyLearnService.keyLearn(e.getKeyCode());
			log.info("Key learned: " + KeyEvent.getKeyText(e.getKeyCode())
					+ " (" + e.getKeyCode() + ")");
		}
	}
}
