package com.midi_automator.view.windows.PreferencesDialog.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.KeyLearnService;

/**
 * Cancels the key learn mode and put the application to the normal state.
 * 
 * @author aguelle
 * 
 */
@Component
public class KeysLearnCancelAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected KeyLearnService keyLearnService;

	@Override
	public void actionPerformed(ActionEvent e) {
		keyLearnService.cancelKeyLearn();
	}
}
