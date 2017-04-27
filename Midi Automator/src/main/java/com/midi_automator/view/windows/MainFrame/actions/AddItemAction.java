package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Component;

/**
 * Opens the add dialog
 * 
 * @author aguelle
 * 
 */
@Component
public class AddItemAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		mainFrame.openAddDialog();
	}
}
