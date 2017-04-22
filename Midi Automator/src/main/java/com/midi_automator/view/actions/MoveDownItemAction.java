package com.midi_automator.view.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.FileListService;

/**
 * Moves the selected item one step down in the list
 * 
 * @author aguelle
 * 
 */
@Component
public class MoveDownItemAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FileListService fileListService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		fileListService
				.moveDownItem(mainFrame.getFileList().getSelectedIndex());
	}
}
