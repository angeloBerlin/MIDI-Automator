package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

/**
 * Opens the edit dialog for the selected item
 * 
 * @author aguelle
 * 
 */
@Component
public class EditItemAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		mainFrame.openEditDialog();
	}
}
