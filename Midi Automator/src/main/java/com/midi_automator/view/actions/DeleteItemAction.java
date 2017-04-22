package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.FileListService;

/**
 * Deletes the selected item from the model
 * 
 * @author aguelle
 * 
 */
@Component
public class DeleteItemAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FileListService fileListService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		fileListService.deleteItem(mainFrame.getFileList().getSelectedIndex());
	}
}
