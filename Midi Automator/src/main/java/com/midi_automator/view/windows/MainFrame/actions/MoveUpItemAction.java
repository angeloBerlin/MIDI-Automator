package com.midi_automator.view.windows.MainFrame.actions;

import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.midi_automator.presenter.services.ItemListService;

/**
 * Moves the selected item one step up in the list
 * 
 * @author aguelle
 * 
 */
@Component
public class MoveUpItemAction extends AbstractMainFramePopUpMenuAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ItemListService fileListService;

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		fileListService.moveUpItem(mainFrame.getItemList().getSelectedIndex());
	}
}
